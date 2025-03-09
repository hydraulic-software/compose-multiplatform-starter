package com.example.myproject

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
