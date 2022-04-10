package com.br.itau.mercadolivre.mercadolivre.validations

import java.lang.annotation.Documented
import javax.validation.Constraint
import kotlin.reflect.KClass


@Documented
@Constraint(validatedBy = [ExistsIdValidator::class])
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class ExistsId(
    val message: String = "Invalid value",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Any>> = [],
    val fieldName: String,
    val domainClass: KClass<*>
)