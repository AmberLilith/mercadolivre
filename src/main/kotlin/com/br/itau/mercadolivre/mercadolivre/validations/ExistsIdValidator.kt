package com.br.itau.mercadolivre.mercadolivre.validations

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.Query
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import kotlin.reflect.KClass
import org.springframework.util.Assert;


class ExistsIdValidator : ConstraintValidator<ExistsId, Any> {
    private var domainAttribute: String? = null
    private var clazz: KClass<*>? = null

    @PersistenceContext
    private val em: EntityManager? = null
    override fun initialize(params: ExistsId) {
        domainAttribute = params.fieldName
        clazz = params.domainClass
    }

    override fun isValid(value: Any, context: ConstraintValidatorContext): Boolean {
        val query: Query = em!!.createQuery("select 1  where " + domainAttribute + "=:value")
        query.setParameter("value", value)
        val list: List<*> = query.getResultList()
        Assert.state(list.size <= 1, "Foi encontrado mais de um $clazz com o atributo $domainAttribute = $value")
        return !list.isEmpty()
    }
}