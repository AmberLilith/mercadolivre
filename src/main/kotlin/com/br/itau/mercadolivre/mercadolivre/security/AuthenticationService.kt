package com.br.itau.mercadolivre.mercadolivre.security

import com.br.itau.mercadolivre.mercadolivre.services.userservices.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

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