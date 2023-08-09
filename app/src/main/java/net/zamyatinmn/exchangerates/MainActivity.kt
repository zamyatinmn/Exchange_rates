package net.zamyatinmn.exchangerates

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import net.zamyatinmn.exchangerates.data.remote.Rate
import net.zamyatinmn.exchangerates.ui.RatesViewInteractor
import net.zamyatinmn.exchangerates.ui.RatesViewModel
import net.zamyatinmn.exchangerates.ui.RatesViewState
import net.zamyatinmn.exchangerates.ui.Sort
import net.zamyatinmn.exchangerates.ui.theme.ExchangeRatesTheme

@AndroidEntryPoint
class MainActivity: ComponentActivity() {
    private val viewModel by viewModels<RatesViewModel>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val state by viewModel.state.collectAsState()
            val interactor = viewModel.interactor

            ExchangeRatesTheme {
                Scaffold(
                    bottomBar = {
                        TabRow(navController, listOf(Screen.Popular, Screen.Favorites))
                    }
                ) {
                    NavHost(
                        modifier = Modifier.padding(it),
                        navController = navController,
                        startDestination = Screen.Popular.route
                    ) {
                        composable(Screen.Popular.route) {
                            RatesScreen(
                                state = state,
                                interactor = interactor,
                                rates = state.rates,
                                onClickSort = { navController.navigate(Screen.Sort.route) },
                            )
                        }
                        composable(Screen.Favorites.route) {
                            RatesScreen(
                                state = state,
                                interactor = interactor,
                                rates = state.rates.filter { rate -> rate.isFavorite },
                                onClickSort = { navController.navigate(Screen.Sort.route) },
                            )
                        }
                        composable(Screen.Sort.route) {
                            Sorting { sort ->
                                interactor.setSort(sort)
                                navController.popBackStack()
                            }
                        }
                    }

                    state.error?.let { errorType ->
                        Toast.makeText(
                            LocalContext.current,
                            "Error - $errorType",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }
}

@Composable
fun RatesScreen(
    state: RatesViewState,
    interactor: RatesViewInteractor,
    rates: List<Rate>,
    onClickSort: () -> Unit,
) {
    Column(Modifier.padding(20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            val list = state.rates.sortedBy { it.id }
            var isExpandedMenu by remember(state) { mutableStateOf(false) }

            Text(
                modifier = Modifier.clickable { isExpandedMenu = true },
                text = state.base?.id ?: "USD"
            )
            DropdownMenu(expanded = isExpandedMenu, onDismissRequest = { isExpandedMenu = false }) {
                list.forEach {
                    DropdownMenuItem(
                        text = { Text(text = it.id) },
                        onClick = {
                            interactor.load(it)
                            isExpandedMenu = false
                        }
                    )
                }
            }
            Spacer(Modifier.weight(1f))
            Box(
                Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(width = 1.dp, color = Color.Yellow, shape = CircleShape)
                    .clickable { onClickSort() }
            )
        }
        Spacer(Modifier.size(20.dp))
        Divider()
        Column(Modifier.verticalScroll(rememberScrollState())) {
            rates.forEach { rate ->
                Row {
                    Text(modifier = Modifier.width(100.dp), text = rate.id)
                    Text(text = rate.rate.toString())
                    Spacer(Modifier.weight(1f))
                    Icon(
                        modifier = Modifier.clickable { interactor.toggleFavorites(rate) },
                        painter = painterResource(id = R.drawable.ic_star),
                        tint = if (rate.isFavorite) Color.Yellow else Color.Black,
                        contentDescription = stringResource(R.string.toggle_favorite_description)
                    )
                }
            }
        }
    }

    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun Sorting(onClickSort: (Sort) -> Unit) {
    val sorts = listOf(
        Sort.NAME_ASCENDING,
        Sort.NAME_DESCENDING,
        Sort.VALUE_ASCENDING,
        Sort.VALUE_DESCENDING
    )
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(id = R.string.sort),
            style = MaterialTheme.typography.titleLarge
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            sorts.forEach { sort ->
                val text = when (sort) {
                    Sort.NAME_ASCENDING -> stringResource(R.string.name_ascending_btn)
                    Sort.NAME_DESCENDING -> stringResource(R.string.name_descending_btn)
                    Sort.VALUE_ASCENDING -> stringResource(R.string.rate_ascending_btn)
                    Sort.VALUE_DESCENDING -> stringResource(R.string.rate_descending_btn)
                }.uppercase()
                Button(onClick = { onClickSort(sort) }) {
                    Text(text = text)
                }
            }
        }
    }
}

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    data object Popular: Screen("popular", R.string.popular)
    data object Favorites: Screen("favorites", R.string.favorites)
    data object Sort: Screen("sort", R.string.sort)
}

@Composable
fun TabRow(navController: NavController, screens: List<Screen>) {
    Surface {
        Row(Modifier.fillMaxWidth()) {
            screens.forEach {
                TextButton(
                    modifier = Modifier
                        .weight(1f)
                        .border(width = 2.dp, color = MaterialTheme.colorScheme.primary),
                    onClick = {
                        navController.navigate(it.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }

                            launchSingleTop = true
                            restoreState = true
                        }
                    }) {
                    Text(text = stringResource(it.resourceId).uppercase())
                }
            }
        }
    }
}