package com.example.android.recipeapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.recipeapplication.adapter.RecipeAdapter
import com.example.android.recipeapplication.adapter.RecipeClickListener
import com.example.android.recipeapplication.room.model.Favorite
import com.example.android.recipeapplication.room.model.Recipe
import com.example.android.recipeapplication.room.viewmodel.RoomRecipeViewModel
import com.example.android.recipeapplication.service.repository.RecipeRepository
import com.example.android.recipeapplication.service.viewmodel.RecipeViewModel
import com.example.android.recipeapplication.service.viewmodel.RecipeViewModelFactory
import java.util.*

class MainActivity : AppCompatActivity(),RecipeClickListener {

    private lateinit var recipeViewModel: RecipeViewModel
    private lateinit var roomRecipeViewModel: RoomRecipeViewModel
    private lateinit var displayList: List<Recipe>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recipeRepository = RecipeRepository()
        val recipeViewModelFactory = RecipeViewModelFactory(recipeRepository)
        recipeViewModel = ViewModelProvider(this, recipeViewModelFactory)[RecipeViewModel::class.java]

        roomRecipeViewModel = ViewModelProvider(this)[RoomRecipeViewModel::class.java]
        roomRecipeViewModel.readAllFav.observe(this){}
        roomRecipeViewModel.readAllRecipe.observe(this){ list ->
            if(list.isEmpty()){
                recipeViewModel.getRecipe()
                recipeViewModel.recipeResponse.observe(this){
                    if (it.isSuccessful){
                        roomRecipeViewModel.insertRecipes(it.body() as List<Recipe>)
                    }
                }

            }
        }

        fetchDataRepeatedly()

        // Recyclerview
        val adapter = RecipeAdapter(applicationContext,this@MainActivity, roomRecipeViewModel)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerview)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)

        // get recipe from room database
        roomRecipeViewModel.readAllRecipe.observe(this){
            displayList = it
            adapter.setRecipesData(displayList)
        }

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

    }

    private fun fetchDataRepeatedly() {
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                try {
                    recipeViewModel.getRecipe()
                    roomRecipeViewModel.insertRecipes(recipeViewModel.recipeResponse.value!!.body() as List<Recipe>)

                } catch (e: Exception) { }
            }
        }, 0, 300000)  // 5 Minutes
    }

    private var simpleCallback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP.or(ItemTouchHelper.DOWN),0){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val startPosition = viewHolder.adapterPosition
            val endPosition = target.adapterPosition

            Collections.swap(displayList, startPosition, endPosition)
            recyclerView.adapter?.notifyItemMoved(startPosition, endPosition)
            return true

        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

        }

    }

    override fun onItemClick(recipe: Recipe, position: Int) {
        val intent = Intent(this, RecipeActivity::class.java)
        intent.putExtra("recipe", recipe)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.sortByName){
            displayList = displayList.sortedBy { it.name }
            roomRecipeViewModel.insertRecipes(displayList)

        }else if(item.itemId == R.id.sortByDifficulty){
            displayList = displayList.sortedBy { it.difficulty }
            roomRecipeViewModel.insertRecipes(displayList)

        }

        return super.onOptionsItemSelected(item)
    }

    override fun onStop() {
        roomRecipeViewModel.insertRecipes(displayList)
        super.onStop()
    }

}

