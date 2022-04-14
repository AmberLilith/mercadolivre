package com.br.itau.mercadolivre.mercadolivre

import com.br.itau.mercadolivre.mercadolivre.exception.InvalidPasswordException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class Crypt {

    companion object{
        fun encode(pass:String):String{
            if(pass.isEmpty()){
                throw InvalidPasswordException("Senha não pode ser vazia!")
            }
            val bCrypt = BCryptPasswordEncoder()
            return bCrypt.encode(pass)
        }
    }
}