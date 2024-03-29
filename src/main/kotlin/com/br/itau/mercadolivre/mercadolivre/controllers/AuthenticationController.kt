package com.br.itau.mercadolivre.mercadolivre.controllers

import com.br.itau.mercadolivre.mercadolivre.dtos.LoginDto
import com.br.itau.mercadolivre.mercadolivre.dtos.TokenDto
import com.br.itau.mercadolivre.mercadolivre.services.TokenService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/auth")
class AuthenticationController {

    @Autowired
    lateinit var authManager:AuthenticationManager
    @Autowired
    lateinit var tokenService:TokenService

    @PostMapping
    fun authenticate(@RequestBody @Valid loginDto: LoginDto):ResponseEntity<Any>{
        val loginDatas: UsernamePasswordAuthenticationToken = loginDto.converter()
        return try {
            val authentication: Authentication = authManager.authenticate(loginDatas)
            val token = tokenService.generateToken(authentication)
            ResponseEntity(TokenDto(token,"Bearer"), HttpStatus.OK)
        }catch (e:Exception){
            print(e.message)
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }
}