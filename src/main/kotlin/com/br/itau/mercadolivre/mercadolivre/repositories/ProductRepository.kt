package com.br.itau.mercadolivre.mercadolivre.repositories

import com.br.itau.mercadolivre.mercadolivre.daos.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProductRepository:JpaRepository<Product,Long> {
    fun findProductByUserId(userId:Long):Optional<Product>
}