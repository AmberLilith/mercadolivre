package com.br.itau.mercadolivre.mercadolivre.validations.category

import com.br.itau.mercadolivre.mercadolivre.services.CategoryService
import org.springframework.beans.factory.annotation.Autowired
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.Query
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import kotlin.reflect.KClass
import org.springframework.util.Assert;


class ExistsCategoryIdValidator : ConstraintValidator<ExistsCategoryId, Long> {
    @Autowired
    lateinit var service:CategoryService

    override fun isValid(value: Long, context: ConstraintValidatorContext): Boolean {

           return (service.findById(value) == null)

    }
}