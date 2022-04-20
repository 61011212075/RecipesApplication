package com.example.android.recipeapplication.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.recipeapplication.room.model.Favorite
import com.example.android.recipeapplication.room.model.Recipe
import retrofit2.Response

@Dao
interface RecipeDao {

    @Insert( onConflict = OnConflictStrategy.REPLACE )
    suspend fun insertRecipes(recipes: List<Recipe>)

    @Insert( onConflict = OnConflictStrategy.ABORT )
    suspend fun insertFavorite(fav: Favorite)

    @Query("SELECT * FROM recipe")
    fun getRecipes(): LiveData<List<Recipe>>

    @Query( "SELECT * FROM favorite")
    fun getFavorites(): LiveData<List<Favorite>>

    @Query("DELETE FROM favorite WHERE recipeId = :recipeId")
    fun removeFavorite(recipeId: String)

}