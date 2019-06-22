package toys.aisamji.bbsrecipeguide.recipe

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import kotlinx.coroutines.*
import toys.aisamji.bbsrecipeguide.*
import toys.aisamji.bbsrecipeguide.data.Command

class RecipeDetailActivity : AppCompatActivity() {

    private val ingredientViewA: TextView
        get() = findViewById(R.id.recipe_detail_ingredient_1)
    private val ingredientViewB: TextView
        get() = findViewById(R.id.recipe_detail_ingredient_2)

    private val altResultViewA: TableRow
        get() = findViewById(R.id.recipe_detail_alt_1)
    private val altResultViewB: TableRow
        get() = findViewById(R.id.recipe_detail_alt_2)

    private val shimmeringView: TextView
        get() = findViewById(R.id.recipe_detail_shimmering)
    private val fleetingView: TextView
        get() = findViewById(R.id.recipe_detail_fleeting)
    private val pulsingView: TextView
        get() = findViewById(R.id.recipe_detail_pulsing)
    private val wellspringView: TextView
        get() = findViewById(R.id.recipe_detail_wellspring)
    private val soothingView: TextView
        get() = findViewById(R.id.recipe_detail_soothing)
    private val hungryView: TextView
        get() = findViewById(R.id.recipe_detail_hungry)
    private val aboundingView: TextView
        get() = findViewById(R.id.recipe_detail_abounding)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume() {
        super.onResume()

        GlobalScope.launch {
            val ingredientA = commands[intent.getIntExtra(extraIngredientA, 0)]
            val ingredientB = commands[intent.getIntExtra(extraIngredientB, 0)]

            MainScope().launch {
                ingredientViewA.text = ingredientA.name
                ingredientViewB.text = ingredientB.name

                ingredientViewA.background = null
                ingredientViewB.background = null
            }

            val alternatives = recipes.forWielder(filter.wielder).involvingBothOf(ingredientA, ingredientB).get()
            val alt1 = commands[alternatives.first().resultId]

            altResultViewA.let {
                MainScope().launch {
                    it.findViewWithTag<TextView>("name").text = alt1.name
                    it.findViewWithTag<TextView>("name").setTextColor(calculateColorFor(alt1))
                    it.findViewWithTag<TextView>("chance").text = getString(R.string.chance, (alternatives.first().successRate * 100).toInt())
                    it.background = null
                }
            }

            altResultViewB.let {
                if (alternatives.count() == 2) {
                    val alt2 = commands[alternatives.last().resultId]
                    val alt2Color = calculateColorFor(alt2)

                    MainScope().launch {
                        it.findViewWithTag<TextView>("name").text = alt2.name
                        it.findViewWithTag<TextView>("name").setTextColor(alt2Color)
                        it.findViewWithTag<TextView>("chance").text = getString(R.string.chance, (alternatives.last().successRate * 100).toInt())
                        it.background = null
                        it.visibility = View.VISIBLE
                    }
                } else {
                    MainScope().launch {
                        it.visibility = View.GONE
                    }
                }
            }


            val recipeType = recipeTypes[alternatives.first().typeId] ?: recipeTypes[alternatives.last().typeId]!!

            recipeType.let {
                MainScope().launch {
                    shimmeringView.text = abilities[it.shimmeringResultId].name
                    fleetingView.text = abilities[it.fleetingResultId].name
                    pulsingView.text = abilities[it.pulsingResultd].name
                    wellspringView.text = abilities[it.wellspringResultId].name
                    soothingView.text = abilities[it.soothingResultId].name
                    hungryView.text = abilities[it.hungryResultId].name
                    aboundingView.text = abilities[it.aboundingResultId].name

                    shimmeringView.background = null
                    fleetingView.background = null
                    pulsingView.background = null
                    wellspringView.background = null
                    soothingView.background = null
                    hungryView.background = null
                    aboundingView.background = null
                }
            }
        }
    }

    private fun calculateColorFor(command: Command): Int {
        return when (command.type) {
            Command.Type.ATTACK -> ContextCompat.getColor(this, R.color.attack_command)
            Command.Type.MAGIC -> ContextCompat.getColor(this, R.color.magic_command)
            Command.Type.ACTION -> ContextCompat.getColor(this, R.color.action_command)
            Command.Type.SHOTLOCK -> ContextCompat.getColor(this, R.color.shotlock_command)
        }
    }

    companion object {
        const val extraIngredientA = "first-ingredient"
        const val extraIngredientB = "second-ingredient"
    }
}
