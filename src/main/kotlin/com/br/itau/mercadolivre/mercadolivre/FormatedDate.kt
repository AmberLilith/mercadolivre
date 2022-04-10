package com.br.itau.mercadolivre.mercadolivre

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class FormatedDate {

    companion object{
        fun formatter(date:LocalDate):String{
            val formatter = DateTimeFormatter.ofPattern("dd-MM-YYYY")
            var formattedDate = date.format(formatter)
            return formattedDate
        }
    }
}