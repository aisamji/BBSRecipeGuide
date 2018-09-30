package toys.aisamji.bbsrecipeguide.data

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

class CommandTable internal constructor(
        dbHelper: BbsOpenHelper
){
    companion object {
        const val NAME = "commands"

        const val ID_COLUMN = "_id"
        const val NAME_COLUMN = "name"
        const val TYPE_COLUMN = "type"
        const val WIELDERS_COLUMN = "wielders"

        val ALL_COLUMNS = arrayOf(ID_COLUMN, NAME_COLUMN, TYPE_COLUMN, WIELDERS_COLUMN)
    }

    private val database: SQLiteDatabase = dbHelper.readableDatabase

    fun all(): Array<Command> {
        var commands: Array<Command> = arrayOf()
        val cursor: Cursor = database.query(NAME, ALL_COLUMNS, null,
                null, null, null, NAME_COLUMN)
        cursor.moveToFirst()

        while (!cursor.isAfterLast) {
            commands = commands.plus(cursorToCommand(cursor))
            cursor.moveToNext()
        }

        cursor.close()
        return commands
    }

    operator fun get(id: Int): Command {
        val command: Command
        val cursor: Cursor = database.query(NAME, ALL_COLUMNS, "$ID_COLUMN = ?",
                arrayOf(id.toString()), null, null, NAME_COLUMN)
        cursor.moveToFirst()

        command = cursorToCommand(cursor)

        cursor.close()
        return command
    }

    private fun cursorToCommand(cursor: Cursor): Command {
        var wielders: Array<Wielder> = arrayOf()
        val wielderMask = cursor.getInt(3)

        for (w: Wielder in Wielder.values()) {
            if (wielderMask.and(w.mask) != 0) {
                wielders += w
            }
        }

        return Command(
                cursor.getInt(0),
                cursor.getString(1),
                Command.Type.values()[cursor.getInt(2) - 1],
                wielders)
    }
}
