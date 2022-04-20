package com.example.android.recipeapplication.room.repository

import androidx.lifecycle.LiveData
import com.example.android.recipeapplication.room.RecipeDao
import com.example.android.recipeapplication.room.model.Favorite
import com.example.android.recipeapplication.room.model.Recipe

class RecipeRepository(private val recipeDao: RecipeDao) {

    val readAllRecipe: LiveData<List<Recipe>> = recipeDao.getRecipes()
    val readAllFav: LiveData<List<Favorite>> = recipeDao.getFavorites()

    suspend fun insertRecipes(recipes: List<Recipe>){
        recipeDao.insertRecipes(recipes)
    }

    suspend fun insertFavorite(fav: Favorite){
        recipeDao.insertFavorite(fav)
    }

    fun removeFavorite(recipeId: String){
        recipeDao.removeFavorite(recipeId)
    }

}