package nl.frankkie.aa_navui.car

import androidx.car.app.CarContext
import androidx.car.app.CarToast
import androidx.car.app.Screen
import androidx.car.app.ScreenManager
import androidx.car.app.model.*
import androidx.car.app.navigation.model.NavigationTemplate
import androidx.core.graphics.drawable.IconCompat
import nl.frankkie.aa_navui.R

class CarMapScreen(carContext: CarContext, val carMapRenderer: CarMapRenderer) :
    Screen(carContext) {

    override fun onGetTemplate(): Template {
        val templateBuilder = NavigationTemplate.Builder()
        val actionStrip = buildActionStrip(carContext)
        templateBuilder.setActionStrip(actionStrip)

        if (isRouteActive()) {
            buildNavigationInstruction(templateBuilder)
        }

        return templateBuilder.build()
    }

    private fun buildNavigationInstruction(templateBuilder: NavigationTemplate.Builder) {
        templateBuilder.apply {
            setBackgroundColor(CarColor.SECONDARY)
            setDestinationTravelEstimate(CarTripInfoUtil.buildETA(carContext))
            setNavigationInfo(CarTripInfoUtil.buildNavigationInfo(carContext))
        }
    }


    //Code below is just for show.

    private fun buildActionStrip(carContext: CarContext): ActionStrip {
        val builder = ActionStrip.Builder()

        //This action is always available (route active or not)
        builder.addAction(
            Action.Builder()
                .setTitle("Melden")
                .setOnClickListener {
                    clickedDummyActionFoo()
                }.build()
        )

        //This action is only available when route is active
        if (isRouteActive()) {

            builder.addAction(
                Action.Builder()
                    .setIcon(
                        CarIcon.Builder(
                            IconCompat.createWithResource(
                                carContext,
                                R.drawable.ic_alt_route
                            )
                        ).build()
                    )
                    .setOnClickListener {
                        clickedDummyActionFoo()
                    }.build()
            )
        }


        builder.addAction(
            Action.Builder()
                .setIcon(
                    CarIcon.Builder(
                        IconCompat.createWithResource(
                            carContext,
                            R.drawable.ic_volume_up
                        )
                    ).build()
                )
                .setOnClickListener {
                    clickedDummyActionFoo()
                }.build()
        )

        //this one is different if route is active
        if (isRouteActive()) {
            builder.addAction(
                Action.Builder()
                    .setIcon(
                        CarIcon.Builder(
                            IconCompat.createWithResource(
                                carContext,
                                R.drawable.ic_close
                            )
                        ).build()
                    )
                    .setOnClickListener {
                        clickedStop()
                    }.build()
            )
        } else {
            builder.addAction(
                Action.Builder()
                    .setIcon(
                        CarIcon.Builder(
                            IconCompat.createWithResource(
                                carContext,
                                R.drawable.ic_search
                            )
                        ).build()
                    )
                    .setOnClickListener {
                        clickedSearch()
                    }.build()
            )
        }

        return builder.build()
    }

    private fun clickedDummyActionFoo() {
        CarToast.makeText(carContext, "This action is only for show", CarToast.LENGTH_LONG).show()
    }

    private fun clickedStop() {
        CarToast.makeText(carContext, "Stop route", CarToast.LENGTH_LONG).show()
        BaseCarAppService.IS_ROUTE_ACTIVE = false
        invalidate()
    }

    private fun clickedSearch() {
        carContext.getCarService(ScreenManager::class.java)
            .push(CarSearchScreen(carContext, carMapRenderer))
    }

    private fun isRouteActive(): Boolean {
        return BaseCarAppService.IS_ROUTE_ACTIVE
    }

}