package com.rutesun

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MainApplication

fun main(vararg args: String) {
    runApplication<MainApplication>(*args)
}