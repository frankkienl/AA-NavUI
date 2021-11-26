package nl.frankkie.aa_navui.car

import android.content.Intent
import android.content.res.Configuration
import androidx.car.app.CarAppService
import androidx.car.app.CarToast
import androidx.car.app.Screen
import androidx.car.app.Session
import androidx.car.app.validation.HostValidator

class BaseCarAppService : CarAppService() {

    private val session = object : Session() {
        override fun onCreateScreen(intent: Intent): Screen {
            CarToast.makeText(
                carContext,
                "Press search button in action strip",
                CarToast.LENGTH_LONG
            ).show()

            val mapRenderer = CarMapRenderer(carContext, lifecycle)

            return CarMapScreen(carContext, mapRenderer)
        }

        override fun onCarConfigurationChanged(newConfiguration: Configuration) {
        }

        override fun onNewIntent(intent: Intent) {
        }
    }

    override fun createHostValidator(): HostValidator {
        return HostValidator.ALLOW_ALL_HOSTS_VALIDATOR
    }

    override fun onCreateSession(): Session {
        return session
    }

    companion object {
        var IS_ROUTE_ACTIVE = false
    }
}