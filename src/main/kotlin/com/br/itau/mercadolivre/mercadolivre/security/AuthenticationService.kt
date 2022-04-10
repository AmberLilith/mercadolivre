package com.br.itau.mercadolivre.mercadolivre.security

import com.br.itau.mercadolivre.mercadolivre.daos.User
import com.br.itau.mercadolivre.mercadolivre.repositories.UserRepository
import com.br.itau.mercadolivre.mercadolivre.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthenticationService:UserDetailsService {
    @Autowired
    lateinit var service: UserService
    override fun loadUserByUsername(username: String): UserDetails {
        var user = service.findByLogin(username)
        if(user.isPresent){
            return user.get()
        }

        throw Exception("Usuário não encontrado!")

    }
}