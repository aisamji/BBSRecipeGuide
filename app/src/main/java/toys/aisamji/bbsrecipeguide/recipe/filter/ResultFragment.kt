package toys.aisamji.bbsrecipeguide.recipe.filter


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
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
class ResultFragment : Fragment(), FilterResponder {

    val TAG = "ResultFragment"

    companion object {
        val extraResult = "result"
    }

    // Widgets
    private val resultGroup: RadioGroup
    get() { return activity!!.findViewById(R.id.filter_command_holder) }

    private val resultScroller: ScrollView
    get() { return activity!!.findViewById(R.id.filter_scroller) }

    private val selectionDisplay: TextView
        get() { return activity!!.findViewById(R.id.filter_selection) }

    // data
    private var selectedId: Int? = null

    override val response: Bundle
    get() {
        val bundle = Bundle()
        if (selectedId != null)
            bundle.putInt(extraResult, selectedId!!)
        return bundle
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        selectedId = arguments?.getInt(extraResult)
        populateCommands()
    }

    private fun populateCommands() {
        resultGroup.removeAllViews()
        for (cmd in commands.all()) {
            val button = layoutInflater.inflate(R.layout.radio_button, resultGroup, false) as RadioButton
            button.text = cmd.name
            button.tag = cmd
            button.setOnCheckedChangeListener { it: CompoundButton, isChecked: Boolean ->
                if (isChecked)
                    selectCommand(it.tag as Command)
            }
            resultGroup.addView(button)

            if (cmd.id == selectedId) {
                resultGroup.check(button.id)
                button.requestFocus()
            }
        }
    }

    private fun selectCommand(cmd: Command) {
        selectedId = cmd.id
        selectionDisplay.text = cmd.name
    }
}
