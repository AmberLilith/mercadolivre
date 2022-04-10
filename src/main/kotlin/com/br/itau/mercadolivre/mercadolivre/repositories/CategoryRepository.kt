package com.br.itau.mercadolivre.mercadolivre.repositories

import com.br.itau.mercadolivre.mercadolivre.daos.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository:JpaRepository<Category, Long> {
    fun findByParentCategoryId(parentCategoryId:Long):Category
}