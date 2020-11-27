package com.rutesun.core.repository

import com.rutesun.core.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long>