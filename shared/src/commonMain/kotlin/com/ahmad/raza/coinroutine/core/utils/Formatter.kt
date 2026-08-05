package com.ahmad.raza.coinroutine.core.utils

expect fun formatFiat(amount: Double, showDecimal: Boolean = true): String

expect fun formatCoinUnit(amount: Double, symbol: String): String

expect fun formatPercentage(amount: Double): String