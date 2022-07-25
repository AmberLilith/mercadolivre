package com.br.itau.mercadolivre.mercadolivre.services.categoryservices

import com.br.itau.mercadolivre.mercadolivre.daos.Category
import com.br.itau.mercadolivre.mercadolivre.exception.NonExistentCategory

class CategoriesListing {

    companion object {

        fun getCategoriesList(categoryId: Long, service: CategoryService): MutableList<String> {

            var categoriesList = ArrayList<String>()
            var mainCategory = service.findById(categoryId)

            if (mainCategory.isPresent) {
                categoriesList.add(mainCategory.get().name)

                if (mainCategory.get().parentCategoryId != 0L) {
                    var pCId = mainCategory.get().parentCategoryId

                    while (pCId != 0L) {
                        var parentCategory = service.findById(pCId)
                        categoriesList.add(parentCategory.get().name)
                        pCId = parentCategory.get().parentCategoryId
                    }
                }
            }else{
                throw NonExistentCategory("Categoria principal não existe!")
            }
            return categoriesList.asReversed()
        }
    }
}

