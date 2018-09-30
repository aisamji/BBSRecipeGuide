package toys.aisamji.bbsrecipeguide.recipe

import android.content.Context
import android.support.v4.content.AsyncTaskLoader
import toys.aisamji.bbsrecipeguide.data.Recipe
import toys.aisamji.bbsrecipeguide.recipe.filter.RecipeFilter
import toys.aisamji.bbsrecipeguide.recipes
import java.util.*

class RecipeLoader(
        context: Context,
        private val filter: RecipeFilter
): AsyncTaskLoader<Collection<Recipe>>(context) {

    inner class FilterObserver: Observer {
        override fun update(p0: Observable?, p1: Any?) {
            this@RecipeLoader.onContentChanged()
        }
    }

    private var data: Collection<Recipe>? = null
    private var observer: FilterObserver? = null

    override fun onStartLoading() {
        if (observer == null) {
            observer = FilterObserver()
            filter.addObserver(observer)
        }

        if (data == null || takeContentChanged()) {
            forceLoad()
        }
    }

    override fun onStopLoading() {
        cancelLoad()
    }

    override fun onReset() {
        onStopLoading()
        data?.let { data = null }
        observer?.let {
            filter.deleteObserver(observer)
            observer = null
        }
    }

    override fun deliverResult(resultData: Collection<Recipe>?) {
        if (isReset) {
            return
        }

        // Keep old data in memory until the new one is delivered
        var oldData = data
        data = resultData

        if (isStarted) {
            super.deliverResult(data)
        }

        oldData = null
    }

    override fun loadInBackground(): Collection<Recipe> {

        val filterer = recipes.forWielder(filter.wielder)
        filter.ability?.let { filterer.thatTeach(it) }
        filter.result?.let { filterer.toCreate(it) }
        if (filter.anyIngredients.isNotEmpty())
            filterer.involvingAnyOf(*filter.anyIngredients)
        return filterer.get();
    }
}
