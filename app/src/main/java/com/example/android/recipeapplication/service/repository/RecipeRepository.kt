package com.example.android.recipeapplication.service.repository

import com.example.android.recipeapplication.service.api.RetrofitInstance
import com.example.android.recipeapplication.room.model.Recipe
import retrofit2.Response

class RecipeRepository {

    suspend fun getRecipe(): Response<List<Recipe>>{
        return RetrofitInstance.recipeApi.getRecipe()
    }
}