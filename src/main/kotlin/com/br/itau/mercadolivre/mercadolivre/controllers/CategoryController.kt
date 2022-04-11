package com.br.itau.mercadolivre.mercadolivre.controllers

import com.br.itau.mercadolivre.mercadolivre.CategoryRegister
import com.br.itau.mercadolivre.mercadolivre.daos.Category
import com.br.itau.mercadolivre.mercadolivre.services.CategoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/categoria")
class CategoryController {
    @Autowired
    lateinit var service: CategoryService

    @PostMapping("/cadastrar")
    fun save(@RequestBody @Valid category: Category): ResponseEntity<Any> {
        return CategoryRegister.save(service,category)
    }
}