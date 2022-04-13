package com.br.itau.mercadolivre.mercadolivre.services.userservices

import com.br.itau.mercadolivre.mercadolivre.daos.User
import com.br.itau.mercadolivre.mercadolivre.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService {
    @Autowired
    lateinit var repository: UserRepository

    fun save(user: User):User{
        return repository.save(user)
    }

    fun findById(id:Long):Optional<User>{
        return repository.findById(id)
    }

    fun findByLogin(login:String):Optional<User>{
        return repository.findByLogin(login)
    }
}