package com.example.android.recipeapplication.service.api

import com.example.android.recipeapplication.room.model.Recipe
import retrofit2.Response
import retrofit2.http.GET

interface RecipeApi {

    @GET("android-test/recipes.json")
    suspend fun getRecipe(): Response<List<Recipe>>

}