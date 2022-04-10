package com.br.itau.mercadolivre.mercadolivre.services

import com.br.itau.mercadolivre.mercadolivre.dtos.UserDto
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.util.*
import javax.servlet.http.HttpServletResponse

class Login {

    companion object{
        fun loginIn(userDto:UserDto,service:UserService,response:HttpServletResponse):ResponseEntity<Any>{
            var user = service.findByLogin(userDto.login).get()

                ?: return ResponseEntity.badRequest().body("Usuário não encontrado!")
            if(!user.comparePassword(userDto.password)){
                return ResponseEntity.badRequest().body("Senha inválida!")
            }

            val issuer = user.id.toString()

            val jwt = Jwts.builder()
                .setIssuer(issuer)
                .setExpiration(Date(System.currentTimeMillis() + 60 * 60  *1000))
                .signWith(SignatureAlgorithm.HS512,"secret").compact()

            val headers = HttpHeaders()
            headers.set("authorization", "BEARER" + jwt)

            /*val cookie = Cookie("jwt",jwt)
            cookie.isHttpOnly = true
            response.addCookie(cookie)*/

            return ResponseEntity("Login efetuado com sucesso!", headers,HttpStatus.OK)
        }
    }
}