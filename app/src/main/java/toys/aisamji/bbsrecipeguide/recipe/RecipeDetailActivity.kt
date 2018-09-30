package toys.aisamji.bbsrecipeguide.recipe

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import toys.aisamji.bbsrecipeguide.*
import toys.aisamji.bbsrecipeguide.data.Command

class RecipeDetailActivity : AppCompatActivity() {

    private lateinit var ingredientViewA: TextView
    private lateinit var ingredientViewB: TextView

    private lateinit var altResultViewA: TableRow
    private lateinit var altResultViewB: TableRow

    private lateinit var shimmeringView: TextView
    private lateinit var fleetingView: TextView
    private lateinit var pulsingView: TextView
    private lateinit var wellspringView: TextView
    private lateinit var soothingView: TextView
    private lateinit var hungryView: TextView
    private lateinit var aboundingView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onStart() {
        super.onStart()

        ingredientViewA = findViewById(R.id.recipe_detail_ingredient_1)
        ingredientViewB = findViewById(R.id.recipe_detail_ingredient_2)

        altResultViewA = findViewById(R.id.recipe_detail_alt_1)
        altResultViewB = findViewById(R.id.recipe_detail_alt_2)

        shimmeringView = findViewById(R.id.recipe_detail_shimmering)
        fleetingView = findViewById(R.id.recipe_detail_fleeting)
        pulsingView = findViewById(R.id.recipe_detail_pulsing)
        wellspringView = findViewById(R.id.recipe_detail_wellspring)
        soothingView = findViewById(R.id.recipe_detail_soothing)
        hungryView = findViewById(R.id.recipe_detail_hungry)
        aboundingView = findViewById(R.id.recipe_detail_abounding)
    }

    override fun onResume() {
        super.onResume()

        val ingredientA = commands[intent.getIntExtra(extraIngredientA, 0)]
        val ingredientB = commands[intent.getIntExtra(extraIngredientB, 0)]

        ingredientViewA.text = ingredientA.name
        ingredientViewB.text = ingredientB.name



        val alternatives = recipes.forWielder(filter.wielder).involvingBothOf(ingredientA, ingredientB).get()

        val alt1 = commands[alternatives.first().resultId]

        altResultViewA.let {
            it.findViewWithTag<TextView>("name").text = alt1.name
            it.findViewWithTag<TextView>("name").setTextColor(calculateColorFor(alt1))
            it.findViewWithTag<TextView>("chance").text = getString(R.string.chance, (alternatives.first().successRate * 100).toInt())
        }

        altResultViewB.let {
            if (alternatives.count() == 2) {
                val alt2 = commands[alternatives.last().resultId]

                it.findViewWithTag<TextView>("name").text = alt2.name
                it.findViewWithTag<TextView>("name").setTextColor(calculateColorFor(alt2))
                it.findViewWithTag<TextView>("chance").text = getString(R.string.chance, (alternatives.last().successRate * 100).toInt())
            } else {
                it.visibility = View.GONE
            }
        }


        val recipeType = recipeTypes[alternatives.first().typeId] ?: recipeTypes[alternatives.last().typeId]!!

        recipeType.let {
            shimmeringView.text = abilities[it.shimmeringResultId].name
            fleetingView.text = abilities[it.fleetingResultId].name
            pulsingView.text = abilities[it.pulsingResultd].name
            wellspringView.text = abilities[it.wellspringResultId].name
            soothingView.text = abilities[it.soothingResultId].name
            hungryView.text = abilities[it.hungryResultId].name
            aboundingView.text = abilities[it.aboundingResultId].name
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
        val extraIngredientA = "first-ingredient"
        val extraIngredientB = "second-ingredient"
    }
}
