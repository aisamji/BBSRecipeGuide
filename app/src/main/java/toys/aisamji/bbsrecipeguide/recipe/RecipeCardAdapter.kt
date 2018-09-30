package toys.aisamji.bbsrecipeguide.recipe

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import toys.aisamji.bbsrecipeguide.R
import toys.aisamji.bbsrecipeguide.commands
import toys.aisamji.bbsrecipeguide.data.Recipe
import kotlin.math.roundToInt

class RecipeCardAdapter(
        context: Context,
        private val recipes: List<Recipe>,
        private var onItemSelectedListener: OnItemSelectedListener?
): RecyclerView.Adapter<RecipeCardAdapter.RecipeCardHolder>() {

    interface OnItemSelectedListener {
        fun onLongClick(recipe: Recipe)
        fun onClick(recipe: Recipe)
    }

    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeCardHolder {
        val view = inflater.inflate(R.layout.recycle_recipe_card, parent, false)
        view.setOnLongClickListener {
            onItemSelectedListener?.onLongClick(it.tag as Recipe)
            true
        }
        view.setOnClickListener {
            onItemSelectedListener?.onClick(it.tag as Recipe)
        }
        return RecipeCardHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeCardHolder, position: Int) {
        holder.result = commands[recipes[position].resultId]
        holder.ingredients = recipes[position].ingredientIds.map { id -> commands[id] }
        holder.successRate = recipes[position].successRate
        holder.recipe = recipes[position]
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    class RecipeCardHolder internal constructor(
            view: View
    ): RecyclerView.ViewHolder(view) {

        private val cardView = view as CardView
        private val resultView: TextView = view.findViewById(R.id.recipe_result)
        private val ingredientsView: TextView = view.findViewById(R.id.recipe_ingredients)
        private val successView: TextView = view.findViewById(R.id.recipe_success)

        var result
        get() = commands[resultView.tag as Int]
        set(value) {
            resultView.text = value.name
            resultView.tag = value.id
        }

        var ingredients
        get() = (ingredientsView.tag as List<Int>).map { id -> commands[id] }
        set(value) {
            ingredientsView.text = "${value[0].name}, ${value[1].name}"
            ingredientsView.tag = value.map { cmd -> cmd.id }
        }

        var successRate
        get() = successView.tag as Double
        set(value) {
            successView.text = (100 * value).roundToInt().toString() + "%"
            successView.tag = value
        }

        var color
        get() = cardView.cardBackgroundColor.defaultColor
        set(value) {
            cardView.setCardBackgroundColor(value)
        }

        var recipe
        get() = cardView.tag as Recipe
        set(value) {
            cardView.tag = value
        }
    }
}
