package com.br.itau.mercadolivre.mercadolivre.repositories

import com.br.itau.mercadolivre.mercadolivre.daos.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository: JpaRepository<User, Long> {
    fun findByLogin(login:String):Optional<User>
}