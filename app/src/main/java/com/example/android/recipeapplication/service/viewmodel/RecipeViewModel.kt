package com.example.android.recipeapplication.service.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.android.recipeapplication.room.model.Recipe
import com.example.android.recipeapplication.service.repository.RecipeRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class RecipeViewModel(private val repository: RecipeRepository): ViewModel() {

    val recipeResponse: MutableLiveData<Response<List<Recipe>>> = MutableLiveData()

    fun getRecipe(){
        viewModelScope.launch {
            val response = repository.getRecipe()
            recipeResponse.value = response
        }
    }

}

class RecipeViewModelFactory(private val repository: RecipeRepository): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RecipeViewModel(repository) as T
    }

}