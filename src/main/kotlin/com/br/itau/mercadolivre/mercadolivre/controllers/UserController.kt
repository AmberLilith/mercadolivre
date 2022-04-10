package com.br.itau.mercadolivre.mercadolivre.controllers

import com.br.itau.mercadolivre.mercadolivre.daos.User
import com.br.itau.mercadolivre.mercadolivre.services.Login
import com.br.itau.mercadolivre.mercadolivre.dtos.UserDto
import com.br.itau.mercadolivre.mercadolivre.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid

@RestController
@RequestMapping("/usuario")
class UserController {
    @Autowired
    lateinit var service: UserService

    @PostMapping("/cadastrar")
    fun save(@RequestBody @Valid userDto: UserDto):ResponseEntity<Any>{
        var user:User = service.save(userDto.converter())
        // TODO: 30/03/2022 retornar 400 caso validação falhe
        return ResponseEntity(user.toString(), HttpStatus.OK)
    }

    @PostMapping("/logar")
    fun login(@RequestBody userDto: UserDto, response:HttpServletResponse):ResponseEntity<Any>{
        return Login.loginIn(userDto,service,response)
    }
}