package nl.frankkie.aa_navui.car

import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.ScreenManager
import androidx.car.app.model.*

class CarSearchScreen(carContext: CarContext, val mapRenderer: CarMapRenderer) : Screen(carContext) {

    private val searchTemplateCallback = object : SearchTemplate.SearchCallback {
        override fun onSearchTextChanged(searchText: String) {
        }

        override fun onSearchSubmitted(searchText: String) {
        }
    }


    override fun onGetTemplate(): Template {
        val templateBuilder = SearchTemplate.Builder(searchTemplateCallback)
        templateBuilder.setHeaderAction(Action.BACK)
        templateBuilder.setInitialSearchText("Just pick any")
        templateBuilder.setLoading(false)
        templateBuilder.setItemList(buildItemList())
        return templateBuilder.build()
    }

    private fun buildItemList() : ItemList {
        val itemListBuilder = ItemList.Builder()
        itemListBuilder.addItem(
            Row.Builder()
                .setTitle("Click me!")
                .setOnClickListener { clickedSearchResult() }
                .build()
        )
        return itemListBuilder.build()
    }

    private fun clickedSearchResult() {
        carContext.getCarService(ScreenManager::class.java).push(CarRoutePreviewScreen(carContext, mapRenderer))
    }
}