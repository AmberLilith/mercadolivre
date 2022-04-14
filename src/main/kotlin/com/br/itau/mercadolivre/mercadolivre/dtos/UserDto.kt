package com.br.itau.mercadolivre.mercadolivre.dtos

import com.br.itau.mercadolivre.mercadolivre.daos.User

class UserDto{

    var login: String = ""

    var pass:String = ""

    constructor(login: String, pass: String) {
        this.login = login
        this.pass = pass
    }

        fun converter():User{
            return User(this.login, this.pass)
        }
}