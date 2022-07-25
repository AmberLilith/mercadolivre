package com.br.itau.mercadolivre.mercadolivre.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class NonExistentCategory(mesage:String): RuntimeException(mesage) {
}