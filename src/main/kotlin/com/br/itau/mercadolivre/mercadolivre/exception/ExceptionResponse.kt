package com.br.itau.mercadolivre.mercadolivre.exception

import java.io.Serializable
import java.util.*

class ExceptionResponse(
    var timestamp: Date,
    var message: String?,
    var details: String
) : Serializable {


}