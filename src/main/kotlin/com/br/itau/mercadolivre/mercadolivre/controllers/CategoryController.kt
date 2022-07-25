package com.br.itau.mercadolivre.mercadolivre.controllers

import com.br.itau.mercadolivre.mercadolivre.services.categoryservices.CategoryRegister
import com.br.itau.mercadolivre.mercadolivre.daos.Category
import com.br.itau.mercadolivre.mercadolivre.services.categoryservices.CategoriesListing
import com.br.itau.mercadolivre.mercadolivre.services.categoryservices.CategoryService
import com.br.itau.mercadolivre.mercadolivre.services.productservices.ProductRegister
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/categoria")
class CategoryController {
    @Autowired
    lateinit var service: CategoryService

    @PostMapping("/cadastrar")
    fun save(@RequestBody @Valid category: Category): ResponseEntity<Any> {
        return CategoryRegister.save(service, category)
    }

    @GetMapping("categoriasMaes/{id}",produces = [MediaType.APPLICATION_JSON_VALUE])
    fun listAllParentCategories(@PathVariable id:Long):ResponseEntity<MutableList<String>>{
        var list = CategoriesListing.getCategoriesList(id,service)
        return ResponseEntity.ok(list)
    }
}