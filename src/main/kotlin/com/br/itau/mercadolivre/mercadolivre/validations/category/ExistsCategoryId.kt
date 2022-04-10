package com.br.itau.mercadolivre.mercadolivre.validations.category

import java.lang.annotation.Documented
import javax.validation.Constraint
import kotlin.reflect.KClass


@Documented
@Constraint(validatedBy = [ExistsCategoryIdValidator::class])
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class ExistsCategoryId(
    val message: String = "Invalid value",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Any>> = []
)