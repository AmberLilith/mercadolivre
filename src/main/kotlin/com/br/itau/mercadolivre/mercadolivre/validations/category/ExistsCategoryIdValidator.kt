package com.br.itau.mercadolivre.mercadolivre.validations.category

import com.br.itau.mercadolivre.mercadolivre.services.categoryservices.CategoryService
import org.springframework.beans.factory.annotation.Autowired
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext


class ExistsCategoryIdValidator : ConstraintValidator<ExistsCategoryId, Long> {
    @Autowired
    lateinit var service: CategoryService

    override fun isValid(value: Long, context: ConstraintValidatorContext): Boolean {

           return (service.findById(value) == null)

    }
}