package com.br.itau.mercadolivre.mercadolivre.services

import com.br.itau.mercadolivre.mercadolivre.daos.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.util.*

@Service
class TokenService {

    val secret = "secret"
    fun generateToken(authentication: Authentication):String{
        val loggedInUser = authentication.principal as User
        val today = Date()
        val expirationDate = Date(today.time + 900000)
        return Jwts.builder()
            .setIssuer("Api Mercado Livre")
            .setSubject(loggedInUser.id.toString())
            .setIssuedAt(today)
            .setExpiration(Date(System.currentTimeMillis() + 60 * 60  *1000))
            .signWith(SignatureAlgorithm.HS512,secret).compact()
    }

    fun isValidToken(token:String?):Boolean{
        try {
            Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token)
            return true;
        }catch (e:Exception){
            return false
        }

    }

    fun getLoggedInUserId(token:String?):Long{
        var claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).body
        return  claims.subject.toLong()
    }
}