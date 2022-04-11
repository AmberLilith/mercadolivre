package com.br.itau.mercadolivre.mercadolivre.controllers

import com.br.itau.mercadolivre.mercadolivre.repositories.UserRepository
import com.br.itau.mercadolivre.mercadolivre.services.TokenService
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthenticationViaTokenFilter(var tokenService:TokenService, var userRepository: UserRepository):OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        var token = recoverToken(request)
        var tokenIsValid = tokenService.isValidToken(token)
        if(tokenIsValid){
            autenticateClient(token)
        }
        filterChain.doFilter(request,response)
    }

    fun autenticateClient(token:String?){
        var userId = tokenService.getUserId(token)
        val user = userRepository.findById(userId).get()
        val authentication = UsernamePasswordAuthenticationToken(user,null,user.authorities)
        SecurityContextHolder.getContext().authentication = authentication
    }

    fun recoverToken(request: HttpServletRequest):String?{
        var token = request.getHeader("Authorization")
        if(token == null || token.isEmpty() || !token.startsWith("Bearer ") ){
            return null
        }

        return token.substring(7,token.length)
    }

}