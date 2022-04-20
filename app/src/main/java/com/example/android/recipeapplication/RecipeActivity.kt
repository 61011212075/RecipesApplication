package com.example.android.recipeapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.utils.widget.ImageFilterView
import com.example.android.recipeapplication.room.model.Recipe
import com.squareup.picasso.Picasso


class RecipeActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)
        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Recipe"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)

        val extras = intent.extras
        if (extras != null) {
            val recipe = extras.get("recipe") as Recipe
            findViewById<TextView>(R.id.name).text = recipe.name + recipe.headline
            findViewById<TextView>(R.id.calories).text =
                if (recipe.fats.isNotEmpty()) getString(R.string.text_cal) + recipe.calories else getString(
                    R.string.blank_cal
                )
            findViewById<TextView>(R.id.carbohydrate).text =
                if (recipe.fats.isNotEmpty()) getString(R.string.text_carbos) + recipe.carbos else getString(
                    R.string.blank_carbos
                )
            findViewById<TextView>(R.id.protein).text =
                if (recipe.fats.isNotEmpty()) getString(R.string.text_protein) + recipe.proteins else getString(
                    R.string.blank_protein
                )
            findViewById<TextView>(R.id.fat).text =
                if (recipe.fats.isNotEmpty()) getString(R.string.text_fat) + recipe.fats else getString(
                    R.string.blank_fat
                )
            findViewById<TextView>(R.id.timeValue).text = recipe.time

            Picasso.with(this)
                .load(recipe.image)
                .placeholder(R.mipmap.ic_img_placeholder)
                .into(findViewById<ImageFilterView>(R.id.image))

            findViewById<TextView>(R.id.description).text = "           " + recipe.description

        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}