package toys.aisamji.bbsrecipeguide.data

import android.database.Cursor

class RecipeTypeTable internal constructor(
        dbHelper: BbsOpenHelper
){
    companion object {
        const val tableName = "recipe_types"
        const val idColumn = "_id"
        const val shimmeringColumn = "shimmering_result"
        const val fleetingColumn = "fleeting_result"
        const val pulsingColumn = "pulsing_result"
        const val wellspringColumn = "wellspring_result"
        const val soothingColumn = "soothing_result"
        const val hungryColumn = "hungry_result"
        const val aboundingColumn = "abounding_result"

        val allColumns = arrayOf(
                idColumn, shimmeringColumn, fleetingColumn, pulsingColumn,
                wellspringColumn, soothingColumn, hungryColumn, aboundingColumn
        )
    }


    private val database = dbHelper.readableDatabase

    fun all(): Array<Recipe.Type> {
        var recipeTypes: Array<Recipe.Type> = arrayOf()
        val cursor: Cursor = database.query(tableName, allColumns, null,
                null, null, null, null)
        cursor.moveToFirst()

        while (!cursor.isAfterLast) {
            recipeTypes = recipeTypes.plus(cursorToType(cursor))
            cursor.moveToNext()
        }

        cursor.close()
        return recipeTypes
    }

    operator fun get(id: Int): Recipe.Type? {
        val type: Recipe.Type
        val cursor: Cursor = database.query(tableName, allColumns, "$idColumn = ?",
                arrayOf(id.toString()), null, null, null)
        if (cursor.count == 0) {
            return null
        }

        cursor.moveToFirst()

        type = cursorToType(cursor)

        cursor.close()
        return type
    }

    private fun cursorToType(cursor: Cursor): Recipe.Type {
        return Recipe.Type(
                cursor.getInt(0),
                cursor.getInt(1),
                cursor.getInt(2),
                cursor.getInt(3),
                cursor.getInt(4),
                cursor.getInt(5),
                cursor.getInt(6),
                cursor.getInt(7)
        )
    }
}
