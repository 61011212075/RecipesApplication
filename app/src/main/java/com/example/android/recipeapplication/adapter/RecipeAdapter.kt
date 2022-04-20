package com.example.android.recipeapplication.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.android.recipeapplication.MainActivity
import com.example.android.recipeapplication.R
import com.example.android.recipeapplication.RecipeActivity
import com.example.android.recipeapplication.room.model.Favorite
import com.example.android.recipeapplication.room.model.Recipe
import com.example.android.recipeapplication.room.viewmodel.RoomRecipeViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recipe_list_item.view.*


class RecipeAdapter(
    private val context: Context,
    private var recipeClickListener: RecipeClickListener,
    private val roomRecipeViewModel: RoomRecipeViewModel
) :
    RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    private var recipeList = emptyList<Recipe>()

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun initialize(item: Recipe, action: RecipeClickListener) {
            itemView.cardRecipe.setOnClickListener {
                action.onItemClick(item, adapterPosition)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recipe_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val currentItem = recipeList[position]
        val favoriteList = roomRecipeViewModel.readAllFav.value
        val favorite = favoriteList?.find { it.recipeId == currentItem.id }

        // Color Favorite Button
        val buttonDrawables: Drawable? = holder.itemView.favoriteButton.background
        val buttonDrawable = DrawableCompat.wrap(buttonDrawables!!)

        if (favorite == null) {
            DrawableCompat.setTint(buttonDrawable, Color.GRAY)
        } else {
            DrawableCompat.setTint(buttonDrawable, Color.RED)
        }
        val difficultyList = arrayOf("easy", "medium", "hard", "expert")
        val difficultyColor = arrayOf(Color.parseColor("#009688"), Color.parseColor("#CDDC39"), Color.parseColor("#FFC107"), Color.RED)
        holder.itemView.favoriteButton.background = buttonDrawable
        holder.itemView.name.text = currentItem.name
        holder.itemView.difficultyValue.text = difficultyList[currentItem.difficulty]
        holder.itemView.difficultyValue.setTextColor(difficultyColor[currentItem.difficulty])
        Picasso.with(context)
            .load(currentItem.thumb)
            .into(holder.itemView.imageThumb)

        holder.itemView.favoriteButton.setOnClickListener {
            val favoriteList = roomRecipeViewModel.readAllFav.value
            val favorite = favoriteList?.find { it.recipeId == currentItem.id }
            if (favorite == null) {
                val fav = Favorite(0, "000", currentItem.id)
                roomRecipeViewModel.insertFavorite(fav)
                DrawableCompat.setTint(buttonDrawable, Color.RED)
            } else {
                roomRecipeViewModel.removeFavorite(currentItem.id)
                DrawableCompat.setTint(buttonDrawable, Color.GRAY)
            }
            holder.itemView.favoriteButton.background = buttonDrawable
        }

        holder.initialize(currentItem, recipeClickListener)

    }

    override fun getItemCount(): Int = recipeList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setRecipesData(recipes: List<Recipe>) {
        this.recipeList = recipes
        notifyDataSetChanged()
    }

}

interface RecipeClickListener {
    fun onItemClick(recipe: Recipe, position: Int)
}