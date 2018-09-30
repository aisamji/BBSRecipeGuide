package toys.aisamji.bbsrecipeguide

import toys.aisamji.bbsrecipeguide.data.*
import toys.aisamji.bbsrecipeguide.recipe.filter.RecipeFilter

lateinit var abilities: AbilityTable
lateinit var recipes: RecipeTable
lateinit var recipeTypes: RecipeTypeTable
lateinit var commands: CommandTable
var filter = RecipeFilter()
