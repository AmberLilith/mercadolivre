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

    @field:NotBlank
    var name:String = ""

    @field:NotNull(message = "Valor não pode ser nulo!")
    @field:Min(1, message = "Valor tem que ser maior que zero!")
    var value:Float = 0f

    @field:Min(0, message = "A quantidade disponível deve ser maior ou igual a zero!")
    var availableQuantity:Int = 0

    @field:Size(min = 3)
    var characteristics = arrayListOf<Characteristic>()

    @field:NotBlank(message = "A descrição não pode ficar em branco!")
    @field:Length(max = 1000, message = "A descrição só pode ter no máximo 1000 caracteres!")
    var description:String = ""

    var categoryId:Long = 0

    var registrationDate = FormatedDate.formatter(LocalDate.now())

    var userId:Long = 0
}