package com.br.itau.mercadolivre.mercadolivre.dtos

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

class LoginDto {

    var login: String = ""
    var pass: String = ""

    constructor(login: String, pass: String) {
        this.login = login
        this.pass = pass
    }

    fun converter():UsernamePasswordAuthenticationToken{
        return UsernamePasswordAuthenticationToken(login, pass)
    }


}