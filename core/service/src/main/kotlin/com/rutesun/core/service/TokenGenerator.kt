package com.rutesun.core.service

import com.rutesun.core.domain.Token

interface TokenGenerator {
    fun generate(): Token
}
