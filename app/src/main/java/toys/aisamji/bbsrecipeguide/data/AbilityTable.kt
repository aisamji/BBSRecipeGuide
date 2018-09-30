package toys.aisamji.bbsrecipeguide.data

import android.database.Cursor

class AbilityTable internal constructor(
        dbHelper: BbsOpenHelper
){
    private val tableName = "abilities"

    private val idColumn = "_id"
    private val nameColumn = "name"

    private val allColumns = arrayOf(idColumn, nameColumn)


    private val database = dbHelper.readableDatabase

    fun all(): Array<Ability> {
        var abilities: Array<Ability> = arrayOf()
        val cursor: Cursor = database.query(tableName, allColumns, null,
                null, null, null, nameColumn)
        cursor.moveToFirst()

        while (!cursor.isAfterLast) {
            abilities = abilities.plus(cursorToAbility(cursor))
            cursor.moveToNext()
        }

        cursor.close()
        return abilities
    }

    operator fun get(id: Int): Ability {
        val ability: Ability
        val cursor: Cursor = database.query(tableName, allColumns, "$idColumn = ?",
                arrayOf(id.toString()), null, null, nameColumn)
        cursor.moveToFirst()

        ability = cursorToAbility(cursor)

        cursor.close()
        return ability
    }

    private fun cursorToAbility(cursor: Cursor): Ability {
        return Ability(
                cursor.getInt(0),
                cursor.getString(1))
    }
}
