package com.br.itau.mercadolivre.mercadolivre.controllers

import com.br.itau.mercadolivre.mercadolivre.dtos.LoginDto
import com.br.itau.mercadolivre.mercadolivre.repositories.UserRepository
import com.br.itau.mercadolivre.mercadolivre.security.AuthenticationService
import com.br.itau.mercadolivre.mercadolivre.services.TokenService
import com.google.gson.Gson
import jdk.nashorn.internal.parser.JSONParser
import org.junit.Assert
import org.junit.jupiter.api.Test
import org.junit.Assert.*
import org.junit.jupiter.api.BeforeEach
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import javax.validation.constraints.AssertTrue

@WebMvcTest(AuthenticationController::class)
@WithMockUser(username = "teste", roles = ["USER", "ADMIN"])
class AuthenticationControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var authManager: AuthenticationManager

    @MockBean
    lateinit var authentication: Authentication


    @MockBean
    lateinit var tokenService: TokenService

    @MockBean
    lateinit var authenticationService: AuthenticationService

    @MockBean
    lateinit var userRepository: UserRepository

/*   @Autowired
    lateinit var authenticationController: AuthenticationController*/

    @Test
    fun given_weHaveAValidLogin_when_weCallAuthenticateFunction_then_returnResponseEntityWithTokenAndStatusCode200(){
        //given //when
        this.mockMvc
            .perform(
                MockMvcRequestBuilders.post("/auth")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{'login':'amber@gmail.com','pass':123}")
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(MockMvcResultMatchers.status().isOk)
            //.andExpect(MockMvcResultMatchers.content().json("{'message':'Id de categoria não pode ser negativo!'}"))


    }
}