package net.zamyatinmn.exchangerates.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Currency(
    @PrimaryKey
    val id: String,
)