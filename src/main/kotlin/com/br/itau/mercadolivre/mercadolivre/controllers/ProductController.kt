package com.br.itau.mercadolivre.mercadolivre.controllers

import com.br.itau.mercadolivre.mercadolivre.daos.Product
import com.br.itau.mercadolivre.mercadolivre.repositories.ProductRepository
import com.br.itau.mercadolivre.mercadolivre.services.TokenService
import com.br.itau.mercadolivre.mercadolivre.services.categoryservices.CategoryService
import com.br.itau.mercadolivre.mercadolivre.services.productservices.ProductRegister
import com.br.itau.mercadolivre.mercadolivre.services.userservices.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@RestController
@RequestMapping("/produto")
class ProductController {
    @Autowired
    lateinit var productRepository: ProductRepository

    @Autowired
    lateinit var categoryService: CategoryService

    @Autowired
    lateinit var request: HttpServletRequest

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var tokenService: TokenService

    @PostMapping("/cadastrar")
    fun save(@RequestBody @Valid product: Product): ResponseEntity<Any> {
        val productRegister = ProductRegister(
            categoryService,
            productRepository,
            request,
            userService,
            tokenService
        )
        return productRegister.save(product)
    }


}