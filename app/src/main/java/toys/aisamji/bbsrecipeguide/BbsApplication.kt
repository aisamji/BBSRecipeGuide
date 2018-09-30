package toys.aisamji.bbsrecipeguide

import android.app.Application
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import toys.aisamji.bbsrecipeguide.data.*
import java.io.File
import java.io.FileOutputStream

class BbsApplication: Application() {

    private val dbHelper = BbsOpenHelper(this)

    override fun onCreate() {
        super.onCreate()

        val preferences = getSharedPreferences("db_info", Context.MODE_PRIVATE)
        if (preferences.getInt(dbVersion, 0) < DB_VERSION) {
            val dbFile = getDatabasePath(DB_NAME)
            copyDatabase(dbFile)
        }
        else {
            throw RuntimeException("Database downgrade not allowed")
        }

        // Initialize global tables
        abilities = AbilityTable(dbHelper)
        recipes = RecipeTable(dbHelper)
        recipeTypes = RecipeTypeTable(dbHelper)
        commands = CommandTable(dbHelper)
    }

    override fun onTerminate() {
        super.onTerminate()

        dbHelper.close()
    }

    private fun copyDatabase(destinationDb: File) {
        val fileIn = assets.open(DB_NAME)
        openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null).close()
        val fileOut = FileOutputStream(destinationDb)

        val buffer = ByteArray(1024)

        while (fileIn.read(buffer) > 0) {
            fileOut.write(buffer)
        }

        fileOut.flush()
        fileOut.close()
        fileIn.close()
    }

    companion object {
        private val dbVersion = "version"
    }
}
