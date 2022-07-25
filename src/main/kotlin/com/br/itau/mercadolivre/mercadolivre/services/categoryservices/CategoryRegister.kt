package com.br.itau.mercadolivre.mercadolivre.services.categoryservices

import com.br.itau.mercadolivre.mercadolivre.daos.Category
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException

class CategoryRegister {

    companion object{
        fun save(service: CategoryService, category: Category):ResponseEntity<Any>{
            if(category.parentCategoryId == 0L){

                service.save(category)

            }else if(category.parentCategoryId < 0L) {

                return ResponseEntity.badRequest().body("{'message':'Id de categoria não pode ser negativo!'}")

            }else{

                val p = service.findById(category.parentCategoryId)

                if(!p.isPresent){
                    return ResponseEntity.badRequest().body("{'message':'Id de categoria informado não existe!'}")

                }else if(p.get().name.equals(category.name)){

                    return ResponseEntity.badRequest().body("{'message':'Uma categoria não pode ser sub-categoria dela mesma!'}")

                }else{

                    service.save(category)
                }
            }
            return ResponseEntity.ok(category)
        }

    }
}