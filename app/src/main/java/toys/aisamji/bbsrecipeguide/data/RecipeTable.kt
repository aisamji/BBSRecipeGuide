package toys.aisamji.bbsrecipeguide.data

import android.database.Cursor
import toys.aisamji.bbsrecipeguide.commands
import toys.aisamji.bbsrecipeguide.recipeTypes

class RecipeTable internal constructor(
        dbHelper: BbsOpenHelper
){
    inner class Filterer internal constructor() {

        private var whereClause: String = ""


        fun thatTeach(ability: Ability): Filterer {
            val types = recipeTypes.all().filter {
                it.aboundingResultId == ability.id ||
                        it.fleetingResultId == ability.id ||
                        it.hungryResultId == ability.id ||
                        it.pulsingResultd == ability.id ||
                        it.shimmeringResultId == ability.id ||
                        it.soothingResultId == ability.id ||
                        it.wellspringResultId == ability.id
            }
            val typeIds = types.map { it.id }

            if (whereClause.isNotEmpty()) whereClause += " AND "
            whereClause += "$typeColumn IN ${listToString(typeIds)}"
            return  this
        }

        fun toCreate(result: Command): Filterer {
            if (whereClause.isNotEmpty()) whereClause += " AND "
            whereClause += "$resultColumn = ${result.id}"
            return this
        }

        fun involvingAnyOf(vararg commands: Command): Filterer {
            val commandIds = commands.map { cmd -> cmd.id }
            if (whereClause.isNotEmpty()) whereClause += " AND "
            whereClause += "($ingredientColumn1 IN ${listToString(commandIds)} OR " +
                            "$ingredientColumn2 IN ${listToString(commandIds)})"
            return this
        }

        fun involvingBothOf(vararg commands: Command): Filterer {
            if (commands.size != 2) {
                throw IllegalArgumentException("involvingAllOf requires exactly 2 commands.")
            }

            val commandIds = commands.map { cmd -> cmd.id }
            if (whereClause.isNotEmpty()) whereClause += " AND "
            whereClause += "(($ingredientColumn1 = ${commandIds.first()} AND $ingredientColumn2 = ${commandIds.last()})" +
                           " OR ($ingredientColumn1 = ${commandIds.last()} AND $ingredientColumn2 = ${commandIds.first()}))"
            return this
        }

        fun forWielder(wielder: Wielder): Filterer {
            if (whereClause.isNotEmpty()) whereClause += " AND "
            whereClause += "($meldersColumn & ${wielder.mask} != 0)"
            return this
        }

        fun get(): Collection<Recipe> {
            var recipes: List<Recipe> = listOf()
            val cursor: Cursor = database.query(tableName, allColumns, whereClause,
                    null, null, null, resultColumn)
            cursor.moveToFirst()

            while (!cursor.isAfterLast) {
                recipes = recipes.plus(cursorToRecipe(cursor))
                cursor.moveToNext()
            }

            cursor.close()
            return recipes
        }

        private fun listToString(values: List<Int>): String {
            val sb = StringBuilder("(")
            values.joinTo(sb)
            sb.append(")")
            return sb.toString()
        }
    }

    private val database = dbHelper.readableDatabase

    fun thatTeach(ability: Ability): Filterer {
        return Filterer().thatTeach(ability)
    }

    fun toCreate(result: Command): Filterer {
        return Filterer().toCreate(result)
    }

    fun involvingAnyOf(vararg commands: Command): Filterer {
        return Filterer().involvingAnyOf(*commands)
    }

    fun involvingBothOf(vararg commands: Command): Filterer {
        return Filterer().involvingBothOf(*commands)
    }

    fun forWielder(wielder: Wielder): Filterer {
        return Filterer().forWielder(wielder)
    }

    operator fun get(id: Int): Recipe {
        val recipe: Recipe
        val cursor: Cursor = database.query(tableName, allColumns, "$idColumn = ?",
                arrayOf(id.toString()), null, null, resultColumn)
        cursor.moveToFirst()

        recipe = cursorToRecipe(cursor)

        cursor.close()
        return recipe
    }

    private fun cursorToRecipe(cursor: Cursor): Recipe {
        val meldersMask = cursor.getInt(6)
        var melders = arrayOf<Wielder>()

        if (meldersMask.and(Wielder.TERRA.mask) != 0) melders += Wielder.TERRA
        if (meldersMask.and(Wielder.VENTUS.mask) != 0) melders += Wielder.VENTUS
        if (meldersMask.and(Wielder.AQUA.mask) != 0) melders += Wielder.AQUA

        return Recipe(
                cursor.getInt(0),
                cursor.getInt(1),
                cursor.getDouble(2),
                cursor.getInt(3),
                arrayOf(cursor.getInt(4), cursor.getInt(5)),
                melders
        )
    }

    companion object {
        private val tableName = "recipes"

        private val idColumn = "_id"
        private val typeColumn = "type"
        private val successColumn = "success_rate"
        private val resultColumn = "result"
        private val ingredientColumn1 = "ingredientA"
        private val ingredientColumn2 = "ingredientB"
        private val meldersColumn = "melders"

        private val allColumns = arrayOf(
                idColumn, typeColumn, successColumn,
                resultColumn, ingredientColumn1, ingredientColumn2, meldersColumn
        )
    }
}
