package nl.frankkie.aa_navui.car

import android.text.SpannableString
import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.ScreenManager
import androidx.car.app.model.*
import androidx.car.app.navigation.model.RoutePreviewNavigationTemplate

class CarRoutePreviewScreen(carContext: CarContext, carMapRenderer: CarMapRenderer) :
    Screen(carContext) {
    override fun onGetTemplate(): Template {
        val templateBuilder = RoutePreviewNavigationTemplate.Builder()
        templateBuilder.setHeaderAction(Action.BACK)
        templateBuilder.setLoading(false)
        templateBuilder.setItemList(buildItemList())
        templateBuilder.setNavigateAction(
            Action.Builder()
                .setOnClickListener { clickedRoute() }
                .setTitle("Let's go!").build()
        )
        return templateBuilder.build()
    }

    private fun buildItemList(): ItemList {
        val itemListBuilder = ItemList.Builder()

        //Create dummy route
        val titleSpannable = SpannableString(" ")
        val durationSpan = DurationSpan.create(60 * 5.toLong()) // 5 minutes
        titleSpannable.setSpan(durationSpan, 0, 1, 0)
        val textSpannable = SpannableString("13:37  \u00b7   ")
        val distanceSpan = DistanceSpan.create(
            Distance.create(
                13.37,
                Distance.UNIT_KILOMETERS
            )
        )
        textSpannable.setSpan(distanceSpan, textSpannable.length - 1, textSpannable.length, 0)
        val row = Row.Builder().setTitle(titleSpannable).addText(textSpannable).build()
        itemListBuilder.addItem(row)

        itemListBuilder.setOnSelectedListener {
            //idk
        }

        return itemListBuilder.build()
    }

    private fun clickedRoute() {
        carContext.getCarService(ScreenManager::class.java).popToRoot()
        BaseCarAppService.IS_ROUTE_ACTIVE = true
    }
}