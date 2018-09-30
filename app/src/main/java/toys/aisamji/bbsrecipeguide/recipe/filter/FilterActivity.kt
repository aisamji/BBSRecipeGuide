package toys.aisamji.bbsrecipeguide.recipe.filter

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.CheckBox
import android.widget.CompoundButton
import kotlinx.android.synthetic.main.activity_filter.*
import toys.aisamji.bbsrecipeguide.R
import toys.aisamji.bbsrecipeguide.commands
import toys.aisamji.bbsrecipeguide.recipe.RecipeLoader

class FilterActivity : AppCompatActivity() {

    enum class By {
        RESULT,
        INGREDIENTS,
        ABIILTY
    }

    companion object {
        val extraFilterBy = "filter-by"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        activity_filter.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        filter_apply.setOnClickListener {
            val tent = Intent()
            val responder = supportFragmentManager.findFragmentByTag("everything") as FilterResponder
            tent.putExtras(responder.response)
            setResult(Activity.RESULT_OK, tent)
            finish()
        }

        // Use specified fragment
        val filterBy: By = intent.extras.getSerializable(extraFilterBy) as By
        lateinit var fragment: Fragment

        when (filterBy) {
            By.RESULT -> fragment = ResultFragment()
            By.INGREDIENTS -> fragment = IngredientFragment()
            By.ABIILTY -> fragment = AbilityFragment()
        }

        fragment.arguments = intent.extras
        supportFragmentManager.beginTransaction()
                .replace(R.id.filter_fragment, fragment, "everything")
                .commit()
    }
}
