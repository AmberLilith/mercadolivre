package com.br.itau.mercadolivre.mercadolivre.services

import com.br.itau.mercadolivre.mercadolivre.daos.Category

class CategoriesListing {

    companion object {

        fun getCategoriesList(category: Category, service: CategoryService): MutableList<String> {

            var categoriesList = ArrayList<String>()
            categoriesList.add(category.name)

            if (category.parentCategoryId == 0L) {
                var pCId = category.parentCategoryId

                while (pCId != 0L) {
                    var parentCategory = service.findById(pCId)
                    categoriesList.add(parentCategory.get().name)
                    pCId = parentCategory.get().parentCategoryId
                }
            }
            return categoriesList.asReversed()
        }
    }
}

