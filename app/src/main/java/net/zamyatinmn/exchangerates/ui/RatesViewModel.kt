package net.zamyatinmn.exchangerates.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import net.zamyatinmn.exchangerates.data.ExchangeRatesRepository
import net.zamyatinmn.exchangerates.data.remote.Rate
import net.zamyatinmn.exchangerates.data.remote.Result
import javax.inject.Inject


@HiltViewModel
class RatesViewModel @Inject constructor(private val repository: ExchangeRatesRepository):
    ViewModel() {

    private val _state = MutableStateFlow(RatesViewState())

    val state = _state.asStateFlow()
    private var activeState
        get() = _state.value
        set(value) {
            _state.value = value
        }

    val interactor = object: RatesViewInteractor {
        override fun load(base: Rate?) {
            activeState = activeState.copy(base = base, isLoading = true)
            viewModelScope.launch {
                activeState = when (val result = repository.getLatest(base)) {
                    is Result.Error -> activeState.copy(
                        error = result.reason,
                        isLoading = false
                    )

                    is Result.Success -> activeState.copy(
                        rates = result.data.applySort(activeState.sort),
                        error = null,
                        isLoading = false
                    )
                }
                repository.getFavorites().collect { favorites ->
                    val list = activeState.rates.toMutableList()
                    list.replaceAll { rate ->
                        if (favorites.any { it.id == rate.id }) {
                            rate.copy(isFavorite = true)
                        } else {
                            rate.copy(isFavorite = false)
                        }
                    }
                    activeState = activeState.copy(rates = list)
                }
            }
        }

        override fun toggleFavorites(rate: Rate) {
            viewModelScope.launch {
                if (rate.isFavorite) {
                    repository.removeFromFavorites(rate)
                } else {
                    repository.addToFavorites(rate)
                }
            }
        }

        override fun setSort(sort: Sort) {
            activeState = activeState.copy(sort = sort, rates = activeState.rates.applySort(sort))
        }
    }

    init {
        interactor.load(null)
    }

    private fun List<Rate>.applySort(sort: Sort): List<Rate> {
        return when (sort) {
            Sort.NAME_ASCENDING -> {
                this.toMutableList().sortedBy { it.id }
            }

            Sort.NAME_DESCENDING -> {
                this.toMutableList().sortedByDescending { it.id }
            }

            Sort.VALUE_ASCENDING -> {
                this.toMutableList().sortedBy { it.rate }
            }

            Sort.VALUE_DESCENDING -> {
                this.toMutableList().sortedByDescending { it.rate }
            }
        }
    }
}

data class RatesViewState(
    val rates: List<Rate> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = true,
    val sort: Sort = Sort.NAME_ASCENDING,
    val base: Rate? = null,
)

interface RatesViewInteractor {
    fun load(base: Rate?)

    fun toggleFavorites(rate: Rate)

    fun setSort(sort: Sort)
}

enum class Sort {
    NAME_ASCENDING,
    NAME_DESCENDING,
    VALUE_ASCENDING,
    VALUE_DESCENDING
}