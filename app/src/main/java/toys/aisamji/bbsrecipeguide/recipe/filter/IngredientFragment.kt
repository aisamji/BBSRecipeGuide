package toys.aisamji.bbsrecipeguide.recipe.filter


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import toys.aisamji.bbsrecipeguide.R
import toys.aisamji.bbsrecipeguide.commands
import toys.aisamji.bbsrecipeguide.data.Command

/**
 * A simple [Fragment] subclass.
 *
 */
class IngredientFragment : Fragment(), FilterResponder {

    val TAG = "IngredientFragment"

    companion object {
        val extraIngredients = "ingredients"
    }

    // Widgets
    private val resultGroup: LinearLayout
        get() { return activity!!.findViewById(R.id.filter_command_holder) }

    private val resultScroller: ScrollView
        get() { return activity!!.findViewById(R.id.filter_scroller) }

    private val selectionDisplay: TextView
        get() { return activity!!.findViewById(R.id.filter_selection) }


    // data
    private var selectedIds = setOf<Int>()

    override val response: Bundle
        get() {
            val bundle = Bundle()
            bundle.putIntArray(extraIngredients, selectedIds.toIntArray())
            return bundle
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ingredient, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        selectedIds = arguments?.getIntArray(extraIngredients)?.toSet() ?: setOf()
        populateCommands()
    }

    private fun populateCommands() {
        resultGroup.removeAllViews()
        for (cmd in commands.all()) {
            val button = layoutInflater.inflate(R.layout.check_box, resultGroup, false) as CheckBox
            button.text = cmd.name
            button.tag = cmd
            button.setOnCheckedChangeListener { it: CompoundButton, isChecked: Boolean ->
                examineCommand(it.tag as Command, isChecked)
            }
            resultGroup.addView(button)

            if (cmd.id in selectedIds) {
                button.isChecked = true
            }
        }
    }


    private fun examineCommand(cmd: Command, toInclude: Boolean) {
        if (toInclude)
            selectedIds += cmd.id
        else
            selectedIds -= cmd.id

        val selectedNames = selectedIds.map { id -> commands[id].name }
        selectionDisplay.text = selectedNames.joinToString()
    }
}
