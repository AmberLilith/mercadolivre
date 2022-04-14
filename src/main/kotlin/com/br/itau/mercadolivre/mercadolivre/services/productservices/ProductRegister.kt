package com.br.itau.mercadolivre.mercadolivre.services.productservices

import com.br.itau.mercadolivre.mercadolivre.controllers.AuthenticationViaTokenFilter
import com.br.itau.mercadolivre.mercadolivre.daos.Product
import com.br.itau.mercadolivre.mercadolivre.repositories.ProductRepository
import com.br.itau.mercadolivre.mercadolivre.services.TokenService
import com.br.itau.mercadolivre.mercadolivre.services.categoryservices.CategoryService
import com.br.itau.mercadolivre.mercadolivre.services.userservices.UserService
import org.springframework.http.ResponseEntity
import javax.servlet.http.HttpServletRequest


class ProductRegister(
    var categoryService: CategoryService,
    var productRepository: ProductRepository,
    var request: HttpServletRequest,
    var userService: UserService,
    var tokenService: TokenService
) {

    fun existsCategory(categoryId: Long): Boolean {
        val category = categoryService.findById(categoryId)
        if (category.isPresent) {
            return true
        }
        return false
    }


    fun getLoggenInUserId(): Long {
        var authenticationViaTokenFilter = AuthenticationViaTokenFilter(tokenService, userService.repository)
        val token = authenticationViaTokenFilter.recoverToken(request)
        return tokenService.getLoggedInUserId(token)
    }


    fun save(product: Product): ResponseEntity<Any> {
        if (existsCategory(product.categoryId)) {
            return ResponseEntity.badRequest().body("Categoria informada não existe!")
        }

        product.userId = getLoggenInUserId()
        productRepository.save(product)
        return ResponseEntity.ok().body(product)
    }

}
