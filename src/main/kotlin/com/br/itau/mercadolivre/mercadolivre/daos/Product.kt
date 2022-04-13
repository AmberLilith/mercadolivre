package com.br.itau.mercadolivre.mercadolivre.daos

import com.br.itau.mercadolivre.mercadolivre.Characteristic
import com.br.itau.mercadolivre.mercadolivre.FormatedDate
import org.hibernate.validator.constraints.Length
import java.time.LocalDate
import javax.persistence.*
import javax.validation.constraints.*
import kotlin.math.min
@Entity
class Product {
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long = 0
    //@field:NotBlank
    var name:String = ""
    //@field:NotNull
    //@field:Min(1)
    var value:Float = 0f
    //@field:Min(0)
    var availableQuantity:Int = 0
    var characteristics = arrayListOf<Characteristic>()
    //@field:Length(max = 1000)
    var description:String = ""
    //@field:NotNull
    var categoryId:Long = 0
    var registrationDate = FormatedDate.formatter(LocalDate.now())
    //@field:NotNull
    var userId:Long = 0
}