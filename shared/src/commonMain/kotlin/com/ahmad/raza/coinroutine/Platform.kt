package com.ahmad.raza.coinroutine

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform