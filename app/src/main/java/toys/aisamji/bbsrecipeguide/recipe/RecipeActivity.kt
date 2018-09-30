package toys.aisamji.bbsrecipeguide.recipe

import android.app.Activity
import android.content.Intent
import android.support.v4.content.Loader
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import android.widget.RadioButton
import kotlinx.android.synthetic.main.piece_recipe_display.*
import kotlinx.android.synthetic.main.piece_recipe_wielder_select.*
import toys.aisamji.bbsrecipeguide.R
import toys.aisamji.bbsrecipeguide.abilities
import toys.aisamji.bbsrecipeguide.commands
import toys.aisamji.bbsrecipeguide.data.Recipe
import toys.aisamji.bbsrecipeguide.data.Wielder
import toys.aisamji.bbsrecipeguide.filter
import toys.aisamji.bbsrecipeguide.recipe.filter.AbilityFragment
import toys.aisamji.bbsrecipeguide.recipe.filter.FilterActivity
import toys.aisamji.bbsrecipeguide.recipe.filter.IngredientFragment
import toys.aisamji.bbsrecipeguide.recipe.filter.ResultFragment

class RecipeActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Collection<Recipe>>,
        RecipeCardAdapter.OnItemSelectedListener {

    // Activity Lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        recipe_display.adapter = RecipeCardAdapter(this, listOf(), this)
        recipe_wielder_terra.setOnCheckedChangeListener { p0, p1 -> changeWielder(p0, p1) }
        recipe_wielder_ventus.setOnCheckedChangeListener { p0, p1 -> changeWielder(p0, p1) }
        recipe_wielder_aqua.setOnCheckedChangeListener { p0, p1 -> changeWielder(p0, p1) }

        // The "elvis" operator. Return maybeWielder if it is not null or return TERRA
        val initialWielder =
                savedInstanceState?.getInt(savedWielder, R.id.recipe_wielder_terra) ?: R.id.recipe_wielder_terra
        findViewById<RadioButton>(initialWielder).isChecked = true

        supportLoaderManager.initLoader(recipeLoaderId, null, this)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putInt(savedWielder, recipe_wielders.checkedRadioButtonId)
    }

    private fun changeWielder(sender: CompoundButton, checkedStatus: Boolean) {
        if (!checkedStatus) return

        when (sender.id) {
            recipe_wielder_terra.id -> filter.wielder = Wielder.TERRA
            recipe_wielder_ventus.id -> filter.wielder = Wielder.VENTUS
            recipe_wielder_aqua.id -> filter.wielder = Wielder.AQUA
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_recipe, menu!!)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val tent = Intent(this, FilterActivity::class.java)
        when (item!!.itemId) {
            R.id.recipe_filter_result -> {
                tent.putExtra(FilterActivity.extraFilterBy, FilterActivity.By.RESULT)
                filter.result?.let { tent.putExtra(ResultFragment.extraResult, it.id) }
                startActivityForResult(tent, requestResultFilter)
            }
            R.id.recipe_filter_ingredients -> {
                tent.putExtra(FilterActivity.extraFilterBy, FilterActivity.By.INGREDIENTS)
                val ingredientIds = filter.anyIngredients.map { cmd -> cmd.id }.toIntArray()
                tent.putExtra(IngredientFragment.extraIngredients, ingredientIds)
                startActivityForResult(tent, requestIngredientFilter)
            }
            R.id.recipe_filter_ability -> {
                tent.putExtra(FilterActivity.extraFilterBy, FilterActivity.By.ABIILTY)
                filter.ability?.let { tent.putExtra(AbilityFragment.extraAbility, it.id) }
                startActivityForResult(tent, requestAbilityFilter)
            }
            R.id.recipe_filter_clear -> {
                filter.clear()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            requestResultFilter ->
                if (resultCode == Activity.RESULT_OK) {
                    val id = data!!.extras?.getInt(ResultFragment.extraResult)
                    filter.result = id?.let { commands[it] }
                }
            requestIngredientFilter ->
                if (resultCode == Activity.RESULT_OK) {
                    val ids = data!!.extras?.getIntArray(IngredientFragment.extraIngredients) ?: intArrayOf()
                    filter.anyIngredients = ids.map { id -> commands[id] }.toTypedArray()
                }
            requestAbilityFilter ->
                if (resultCode == Activity.RESULT_OK) {
                    val id = data!!.extras?.getInt(AbilityFragment.extraAbility)
                    filter.ability = id?.let { abilities[it] }
                }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    // region Loader
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Collection<Recipe>> {
        return RecipeLoader(this, filter)
    }

    override fun onLoadFinished(loader: Loader<Collection<Recipe>>, data: Collection<Recipe>?) {
        (recipe_display.adapter as RecipeCardAdapter)
        recipe_display.adapter = RecipeCardAdapter(this, data!!.toList(), this)
        recipe_loading.visibility = View.GONE
    }

    override fun onLoaderReset(loader: Loader<Collection<Recipe>>) {}

    // endregion

    // region Item Selection
    override fun onLongClick(recipe: Recipe) { }

    override fun onClick(recipe: Recipe) {
        val tent = Intent(this, RecipeDetailActivity::class.java)
        tent.putExtra(RecipeDetailActivity.extraIngredientA, recipe.ingredientIds.first())
        tent.putExtra(RecipeDetailActivity.extraIngredientB, recipe.ingredientIds.last())
        startActivity(tent)
    }

    // endregion

    companion object {
        private val requestIngredientFilter = 23
        private val requestResultFilter = 24
        private val requestAbilityFilter = 25

        private val recipeLoaderId = 4

        private val savedWielder = "wielder"
    }

}
