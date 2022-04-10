package com.br.itau.mercadolivre.mercadolivre.daos

import com.br.itau.mercadolivre.mercadolivre.FormatedDate
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.time.LocalDate
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity
class User:UserDetails{
        @field:Id
        @field:GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0
        @field:NotBlank(message = "Email não pode estar em branco!")
        @field:Email(message = "Email informado é inválido!")
        @field:Column(unique = true)
        var login: String = ""
        @field:NotBlank
        @field:Size(min = 6)
        var pass: String = ""
        var registerDate = FormatedDate.formatter(LocalDate.now())
        // TODO: 07/04/2022 Ver se realmente precisa de lista de perfis 

        constructor(email:String, password:String){
                val bCrypt = BCryptPasswordEncoder()
                this.login = email
                this.pass = bCrypt.encode(password)
        }

        fun comparePassword(passwordFromRequest: String):Boolean{
                val bCrypt = BCryptPasswordEncoder()
                return bCrypt.matches(passwordFromRequest,this.pass)
        }

        override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
                return mutableListOf()
        }

        override fun getPassword(): String {
                return this.pass
        }

        override fun getUsername(): String {
                return this.login
        }

        override fun isAccountNonExpired(): Boolean {
                return true
        }

        override fun isAccountNonLocked(): Boolean {
                return true
        }

        override fun isCredentialsNonExpired(): Boolean {
                return true
        }

        override fun isEnabled(): Boolean {
                return true
        }

        override fun toString(): String {
                return "{\n'id':${this.id},\n'login':'${this.login}'\n}"
        }

}