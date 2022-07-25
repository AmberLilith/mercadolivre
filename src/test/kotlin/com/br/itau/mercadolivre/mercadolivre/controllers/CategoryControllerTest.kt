package com.br.itau.mercadolivre.mercadolivre.controllers

import com.br.itau.mercadolivre.mercadolivre.daos.Category
import com.br.itau.mercadolivre.mercadolivre.repositories.UserRepository
import com.br.itau.mercadolivre.mercadolivre.security.AuthenticationService
import com.br.itau.mercadolivre.mercadolivre.services.TokenService
import com.br.itau.mercadolivre.mercadolivre.services.categoryservices.CategoryService
import com.google.gson.Gson
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.*


@WebMvcTest(CategoryController::class)
@WithMockUser(username = "teste", roles = ["USER", "ADMIN"])
class CategoryControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var gson: Gson

    @MockBean
    lateinit var service: CategoryService

    @MockBean
    lateinit var authenticationService: AuthenticationService

    @MockBean
    lateinit var tokenService: TokenService

    @MockBean
    lateinit var userRepository: UserRepository


    @Test
    fun given_weHaveAValidCategoryWithParentCategoryIdEqual0_when_callingSaveMethod_then_returnResponseEntityWithStatusCodeOkAndCategory() {
        //given //when
        this.mockMvc
            .perform(
                post("/categoria/cadastrar")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"id\":1,\"name\":\"informática\",\"parentCategoryId\":0}")
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("informática"))
            .andExpect(content().json("{\"id\":1,\"name\":\"informática\",\"parentCategoryId\":0}"))
    }

    @Test
    fun given_aValidCategoryWithParentCategoryIdSmallerThen0_when_callingSaveMethod_then_returnResponseEntityWithStatusCodeBadRequest() {
        //given //when
        this.mockMvc
            .perform(
                post("/categoria/cadastrar")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"id\":1,\"name\":\"informática\",\"parentCategoryId\":-10}")
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest)
            .andExpect(content().json("{'message':'Id de categoria não pode ser negativo!'}"))
    }

    @Test
    fun given_aValidCategoryWithNonExistentParentCategoryId_when_callingSaveMethod_then_returnResponseEntityWithStatusCodeBadRequest() {
        //given //when
        this.mockMvc
            .perform(
                post("/categoria/cadastrar")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"id\":1,\"name\":\"informática\",\"parentCategoryId\":1}")
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest)
            .andExpect(content().json("{'message':'Id de categoria informado não existe!'}"))
    }

    @Test
    fun given_aValidCategoryWithExistentParentCategoryId_when_callingSaveMethod_then_returnResponseEntityWithStatusCodeOkAndCategory() {

        //given
        val CategoryOptional: Optional<Category> = Optional.of(Category("periféricos", 1))
        `when`(service.findById(1)).thenReturn(CategoryOptional)

        //when
        this.mockMvc
            .perform(
                post("/categoria/cadastrar")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"id\":1,\"name\":\"informática\",\"parentCategoryId\":1}")
                    .accept(MediaType.APPLICATION_JSON)
            )

            //then
            .andExpect(status().isOk)
            .andExpect(content().json("{'id':1,name:'informática','parentCategoryId':1}"))

    }

    @Test
    fun given_aValidCategoryWithNameEqualParentCategoryName_when_callingSaveMethod_then_returnResponseEntityWithStatusCodeOkAndCategory() {

        //given
        val CategoryOptional: Optional<Category> = Optional.of(Category("informática", 1))
        `when`(service.findById(1)).thenReturn(CategoryOptional)

        //when
        this.mockMvc
            .perform(
                post("/categoria/cadastrar")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"id\":1,\"name\":\"informática\",\"parentCategoryId\":1}")
                    .accept(MediaType.APPLICATION_JSON)
            )

            //then
            .andExpect(status().isBadRequest)
            .andExpect(content().json("{'message':'Uma categoria não pode ser sub-categoria dela mesma!'}"))

    }


    @Test
    fun given_aValidCategoryWithAlreadyExistentName_when_callingSaveMethod_then_returnResponseEntityWithStatusCodeBadRequest() {

        //given
        val CategoryOptional: Optional<Category> = Optional.of(Category("informática", 1))
        `when`(service.findById(1)).thenReturn(CategoryOptional)

        //when
        this.mockMvc
            .perform(
                post("/categoria/cadastrar")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"id\":1,\"name\":\"informática\",\"parentCategoryId\":1}")
                    .accept(MediaType.APPLICATION_JSON)
            )

            //then
            .andExpect(status().isBadRequest)
            .andExpect(content().json("{'message':'Uma categoria não pode ser sub-categoria dela mesma!'}"))

    }

   /* @Test
    fun test(){
        //given
        var categoriesList = arrayListOf("mouse","periféricos","informática")
        var optionalCategoriesList: Optional<ArrayList<String>> = Optional.of(categoriesList)
        `when`(CategoriesListing.getCategoriesList(1, any())).thenReturn(categoriesList)
        //when
        this.mockMvc
            .perform(
                get("/categoria/categoriasMaes/1")
            )
            //then
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0]").value("mouse"))
            .andExpect(jsonPath("$[1]").value("periféricos"))
            .andExpect(jsonPath("$[2]").value("informática"))

    }*/
}