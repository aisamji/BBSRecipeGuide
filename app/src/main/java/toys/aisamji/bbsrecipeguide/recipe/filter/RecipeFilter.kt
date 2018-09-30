package toys.aisamji.bbsrecipeguide.recipe.filter

import toys.aisamji.bbsrecipeguide.data.Ability
import toys.aisamji.bbsrecipeguide.data.Command
import toys.aisamji.bbsrecipeguide.data.Wielder
import java.util.*

class RecipeFilter: Observable() {

    private var _ability: Ability? = null

    var ability: Ability?
    get() { return _ability }
    set(value) {
        if (_ability != value) {
            clearInternal()
            _ability = value
            setChanged()
            notifyObservers()
        }
    }

    private var _anyIngredients = arrayOf<Command>()

    var anyIngredients: Array<Command>
        get() { return _anyIngredients }
        set(value) {
            if (!_anyIngredients.contentEquals(value)) {
                clearInternal()
                _anyIngredients = value
                setChanged()
                notifyObservers()
            }
        }

    private var _result: Command? = null

    var result: Command?
        get() { return _result }
        set(value) {
            if (_result != value) {
                clearInternal()
                _result = value
                setChanged()
                notifyObservers()
            }
        }

    var wielder: Wielder = Wielder.TERRA
        set(value) {
            if (field != value) {
                field = value
                setChanged()
                notifyObservers()
            }
        }

    fun clear() {
        clearInternal()
        setChanged()
        notifyObservers()
    }

    private fun clearInternal() {
        _anyIngredients = arrayOf()
        _result = null
        _ability = null
    }
}
