package com.br.itau.mercadolivre.mercadolivre.dtos

import com.br.itau.mercadolivre.mercadolivre.daos.User
import com.fasterxml.jackson.annotation.JsonIgnore

class UserDto{

    var login: String = ""

    var password:String = ""

    constructor(email: String, password: String) {
        this.login = email
        this.password = password
    }

        fun converter():User{
            return User(this.login, this.password)
        }
}