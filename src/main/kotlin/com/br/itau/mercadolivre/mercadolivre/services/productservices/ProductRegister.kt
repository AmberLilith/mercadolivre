package com.br.itau.mercadolivre.mercadolivre.services.productservices

import com.br.itau.mercadolivre.mercadolivre.Characteristic
import com.br.itau.mercadolivre.mercadolivre.controllers.AuthenticationViaTokenFilter
import com.br.itau.mercadolivre.mercadolivre.daos.Product
import com.br.itau.mercadolivre.mercadolivre.repositories.ProductRepository
import com.br.itau.mercadolivre.mercadolivre.services.TokenService
import com.br.itau.mercadolivre.mercadolivre.services.categoryservices.CategoryService
import com.br.itau.mercadolivre.mercadolivre.services.userservices.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.*
import javax.servlet.http.HttpServletRequest
import kotlin.collections.ArrayList


class ProductRegister(var categoryService: CategoryService,var productRepository: ProductRepository) {



    @Autowired
    lateinit var request: HttpServletRequest

    fun isNameEmpty(name: String): Any {
        if (name.isEmpty()) {
            return ResponseEntity.badRequest().body("O nome do produto deve ser informado!")
        }

        return true
    }

    fun isValueNullOrSmallerOrEqualsZero(value: Float): Any {
        if (value == null || value <= 0L) {
            return ResponseEntity.badRequest().body("Valor do produto deve ser maior que zero e não pode ser nulo!")
        }
        return true
    }

    fun isAvailableQuantitySmallerThenZero(availableQuantity: Int): Any {
        if (availableQuantity < 0) {
            return ResponseEntity.badRequest().body("A quantidade disponível não pode ser menor que zero")
        }
        return true
    }

    fun isDescriptionEmptyOrGreaterThen1000(description: String): Any {
        if (description.length > 1000) {
            return ResponseEntity.badRequest().body("A descrição não pode ter mais que 1000 caracteres!")
        } else if (description.isNullOrBlank()) {
            return ResponseEntity.badRequest().body("A descrição não pode estar em branco!")
        }
        return true

    }


    fun existsCategory(categoryId: Long): Any {
        val category = categoryService.findById(categoryId)
        if (!category.isPresent) {
            return ResponseEntity.badRequest().body("Categoria informada não existe!")
        }
        return true
    }

    fun HasCharacteristicsMinLength(characteristics: ArrayList<Characteristic>): Any {
        if (characteristics.size < 3) {
            return ResponseEntity.badRequest().body("Produto deve ter pelo menos 3 características")
        }
        return true
    }

    fun getLoggenInUserId(): Long {
         var userService = UserService()
        var tokenService = TokenService()
        var authenticationViaTokenFilter = AuthenticationViaTokenFilter(tokenService,userService.repository)
        val token = authenticationViaTokenFilter.recoverToken(request)
        return tokenService.getLoggedInUserId(token)
    }


    fun save(product: Product): ResponseEntity<Any> {
        isNameEmpty(product.name)
        isAvailableQuantitySmallerThenZero(product.availableQuantity)
        isDescriptionEmptyOrGreaterThen1000(product.description)
        isValueNullOrSmallerOrEqualsZero(product.value)
        existsCategory(product.categoryId)
        product.userId = getLoggenInUserId()
        productRepository.save(product)

        return ResponseEntity.ok().body(product)
    }

}
