package com.example.rakshak20

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform