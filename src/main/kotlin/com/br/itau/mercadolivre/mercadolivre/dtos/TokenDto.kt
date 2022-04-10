package com.br.itau.mercadolivre.mercadolivre.dtos

class TokenDto {
    var token:String = ""
    var type:String = ""

    constructor(token: String, type: String) {
        this.token = token
        this.type = type
    }
}