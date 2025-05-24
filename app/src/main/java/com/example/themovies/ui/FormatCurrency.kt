package com.example.themovies.ui

import java.util.Locale

fun formatCurrency(value: Long?): String {
    if (value == null || value == 0L) return "N/A"
    val millions = value / 1_000_000.0
    return String.format(Locale.getDefault(), "%.2f M", millions)
}

