package com.br.itau.mercadolivre.mercadolivre.services

import com.br.itau.mercadolivre.mercadolivre.daos.Category
import com.br.itau.mercadolivre.mercadolivre.repositories.CategoryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class CategoryService {
    @Autowired
    lateinit var repository:CategoryRepository

    fun save(category: Category):Category{
        return repository.save(category)
    }

    fun findById(id:Long):Optional<Category>{
        return repository.findById(id)
    }
}