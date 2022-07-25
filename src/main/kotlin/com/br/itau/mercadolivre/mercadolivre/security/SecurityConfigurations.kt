package com.br.itau.mercadolivre.mercadolivre.security

import com.br.itau.mercadolivre.mercadolivre.controllers.AuthenticationViaTokenFilter
import com.br.itau.mercadolivre.mercadolivre.repositories.UserRepository
import com.br.itau.mercadolivre.mercadolivre.services.TokenService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@EnableWebSecurity
@Configuration
class SecurityConfigurations:WebSecurityConfigurerAdapter(){
    @Autowired
    lateinit var authenticationService: AuthenticationService
    @Autowired
    lateinit var  tokenService: TokenService
    @Autowired
    lateinit var userRepository: UserRepository

    @Bean
    override fun authenticationManager(): AuthenticationManager {
        return super.authenticationManager()
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth!!.userDetailsService(authenticationService).passwordEncoder(BCryptPasswordEncoder())
    }

    override fun configure(web: WebSecurity?) {
        web!!
            .ignoring()
            .antMatchers("/h2-console/**");
    }

    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
            .antMatchers("/usuario/cadastrar").permitAll()
            .antMatchers("/auth").permitAll()
            .anyRequest().authenticated()
            .and().csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().addFilterBefore(AuthenticationViaTokenFilter(tokenService, userRepository),UsernamePasswordAuthenticationFilter::class.java)

    }
}