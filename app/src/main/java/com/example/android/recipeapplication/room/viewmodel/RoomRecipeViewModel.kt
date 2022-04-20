package com.example.android.recipeapplication.room.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.recipeapplication.room.RecipeDatabase
import com.example.android.recipeapplication.room.model.Favorite
import com.example.android.recipeapplication.room.model.Recipe
import com.example.android.recipeapplication.room.repository.RecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class RoomRecipeViewModel(application: Application): AndroidViewModel(application) {

    val readAllRecipe: LiveData<List<Recipe>>
    val readAllFav: LiveData<List<Favorite>>
    private val repository: RecipeRepository
    var responseFavorite: MutableLiveData<Int> = MutableLiveData()

    init {
        val recipeDao = RecipeDatabase.getDatabase(application).recipeDao()
        repository = RecipeRepository(recipeDao)
        readAllRecipe = repository.readAllRecipe
        readAllFav = repository.readAllFav
    }

    fun insertRecipes(recipes: List<Recipe>){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertRecipes(recipes)
        }
    }

    fun insertFavorite(fav: Favorite){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertFavorite(fav)
        }
    }

    fun removeFavorite(recipeId: String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeFavorite(recipeId)
        }
    }

//    fun isFavorite(recipeId: String){
//        val response = repository.isFavorite(recipeId)
//        responseFavorite.value = response
//    }
}