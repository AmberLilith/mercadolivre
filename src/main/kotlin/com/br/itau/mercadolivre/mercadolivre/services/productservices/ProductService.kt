package com.br.itau.mercadolivre.mercadolivre.services.productservices

import com.br.itau.mercadolivre.mercadolivre.daos.Product
import com.br.itau.mercadolivre.mercadolivre.repositories.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProductService {
    @Autowired
    lateinit var repository:ProductRepository

    fun save(product: Product):Product{
        return repository.save(product)
    }
}