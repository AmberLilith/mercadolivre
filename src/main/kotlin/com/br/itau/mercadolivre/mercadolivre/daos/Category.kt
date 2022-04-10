package com.br.itau.mercadolivre.mercadolivre.daos

import com.br.itau.mercadolivre.mercadolivre.validations.ExistsId
import com.br.itau.mercadolivre.mercadolivre.validations.category.ExistsCategoryId
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
class Category {
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long = 0
    @field:NotBlank(message = "Nome da categoria não foi informado!")
    @field:Column(unique = true)
    var name:String
    var parentCategoryId: Long = -1

    constructor(name: String, parentCategoryId: Long) {
        this.name = name
        this.parentCategoryId = parentCategoryId
    }

    override fun toString(): String {
        return "Category(id=$id, name='$name', parentCategoryId=$parentCategoryId)"
    }


}