## Description

* When the app is first launched, fetch the recipes JSON from the API and store it in the app DB.
* On subsequent launches, use the data from the local DB.

```bash
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
```
* Have a 5-minute recurring background job to sync the local DB with the API
```bash
private fun fetchDataRepeatedly() {
    val timer = Timer()
    timer.scheduleAtFixedRate(object : TimerTask() {
        override fun run() {
            try {
                recipeViewModel.getRecipe()
                roomRecipeViewModel.insertRecipes(recipeViewModel.recipeResponse.value!!.body() as List<Recipe>)

            } catch (e: Exception) { }
        }
    }, 0, 300000) // 5 Minutes
}
```
* Be able to mark a recipe(s) as favorite
```bash
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
```

* Be able to sort and re-arrange the list then the change will be saved to the local DB.
* Sort by name and difficulty
```bash
if (item.itemId == R.id.sortByName){
    displayList = displayList.sortedBy { it.name }
    roomRecipeViewModel.insertRecipes(displayList)

}else if(item.itemId == R.id.sortByDifficulty){
    displayList = displayList.sortedBy { it.difficulty }
    roomRecipeViewModel.insertRecipes(displayList)

}
```

* Re-arrange the list
```bash
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
```





