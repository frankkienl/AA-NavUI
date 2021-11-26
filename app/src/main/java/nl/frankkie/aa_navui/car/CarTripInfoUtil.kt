package nl.frankkie.aa_navui.car

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.VectorDrawable
import android.text.SpannableString
import android.text.Spanned
import androidx.car.app.CarContext
import androidx.car.app.model.*
import androidx.car.app.navigation.model.*
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.IconCompat
import nl.frankkie.aa_navui.R
import java.util.*

object CarTripInfoUtil {

    //Create fake ETA
    fun buildETA(carContext: CarContext): TravelEstimate {
        //74.1 km
        val distance = Distance.create(74100.0 / 1000.0, Distance.UNIT_KILOMETERS)
        //47 min
        val durationInSeconds = 2820L
        val dateTimeWithZone = DateTimeWithZone.create(
            System.currentTimeMillis() + (durationInSeconds * 1000),
            TimeZone.getDefault()
        )
        val travelEstimate = TravelEstimate.Builder(
            distance,
            dateTimeWithZone
        ).apply {
            setRemainingTimeSeconds(durationInSeconds)
        }
        travelEstimate.setRemainingTimeColor(CarColor.GREEN)
        return travelEstimate.build()
    }

    //Create fake navigation info
    fun buildNavigationInfo(carContext: CarContext): NavigationTemplate.NavigationInfo {
        val routingInfoBuilder = RoutingInfo.Builder()
        val currentStep = buildCurrentStep(carContext)
        //1.3 km
        val distance = Distance.create(1300.0 / 1000.0, Distance.UNIT_KILOMETERS)
        routingInfoBuilder.setCurrentStep(currentStep, distance)
        routingInfoBuilder.setNextStep(buildNextStep(carContext))
        return routingInfoBuilder.build()
    }

    //Create fake step
    fun buildCurrentStep(carContext: CarContext): Step {
        var cue = ""
        val roadText = "R4"
        val detailText = "Keep right"
        cue = roadText + "\n" + detailText
        val roadSignBitmap = CarSignsUtil.buildRoadSignBitmap(roadText)
        val cueWithImages = SpannableString(cue)
        if (roadSignBitmap != null) {
            val signSpan = CarIconSpan.create(
                CarIcon.Builder(IconCompat.createWithBitmap(roadSignBitmap)).build(),
                CarIconSpan.ALIGN_CENTER
            )
            cueWithImages.setSpan(
                signSpan,
                cue.indexOf(roadText),
                cue.indexOf(roadText) + roadText.length,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )
        }
        //This is stupid btw; cue should be something you can add later to the builder,
        //instead of with the constructor already. Oh well, it works.
        val stepBuilder = Step.Builder(cueWithImages)
        val maneuverType = Maneuver.TYPE_DEPART
        val maneuverBuilder = Maneuver.Builder(maneuverType)
        val icon = getNavigationDirectionCarIcon(carContext)
        maneuverBuilder.setIcon(icon)
        stepBuilder.setManeuver(maneuverBuilder.build())
        stepBuilder.setRoad(roadText)
        //lanes
        val fakeLaneDirections =
            listOf(LaneDirection.SHAPE_STRAIGHT, LaneDirection.SHAPE_SHARP_RIGHT)
        fakeLaneDirections.forEach { fakeLaneDirection ->
            val laneBuilder = Lane.Builder()
            laneBuilder.addDirection(
                LaneDirection.create(
                    fakeLaneDirection,
                    (fakeLaneDirection == LaneDirection.SHAPE_SHARP_RIGHT)
                )
            )
            stepBuilder.addLane(laneBuilder.build())
        }
        stepBuilder.setLanesImage(
            CarIcon.Builder(
                IconCompat.createWithBitmap(
                    buildLanesImage(
                        carContext,
                        fakeLaneDirections
                    )
                )
            ).build()
        )
        return stepBuilder.build()
    }

    fun buildNextStep(carContext: CarContext): Step {
        val stepBuilder = Step.Builder("Next")
        val maneuverBuilder = Maneuver.Builder(Maneuver.TYPE_TURN_SHARP_RIGHT)
        maneuverBuilder.setIcon(getNavigationDirectionCarIcon(carContext))
        stepBuilder.setManeuver(maneuverBuilder.build())
        return stepBuilder.build()
    }

    fun getNavigationDirectionCarIcon(carContext: CarContext): CarIcon {
        val iconId = R.drawable.ic_alt_route
        return CarIcon.Builder(IconCompat.createWithResource(carContext, iconId)).build()
    }

    fun buildLanesImage(carContext: CarContext, lanes: List<Int>): Bitmap {
        //294x44dp //TODO: Update to new size, 500x74
        val bitmap = Bitmap.createBitmap(294, 44, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val laneImageWidth = 36
        val laneImageHeight = 31
        val marginX = 8
        val marginY = 6F
        var index = 0
        val bitmapCenter = (bitmap.width / 2)
        val lanesCenter = (((laneImageWidth + marginX) * lanes.size) / 2)
        val lanesLeft = bitmapCenter - lanesCenter
        lanes.forEach { laneGuidance ->
            val drawableResInt = getDirectionImageForLaneDirection(laneGuidance)
            val drawable = ContextCompat.getDrawable(carContext, drawableResInt)
            val vectorDrawable = drawable as? VectorDrawable
            vectorDrawable?.setBounds(0, 0, laneImageWidth, laneImageHeight)
            val translateX = ((laneImageWidth + marginX) * index) + lanesLeft
            canvas.translate(translateX.toFloat(), marginY)
            vectorDrawable?.draw(canvas)
            vectorDrawable?.alpha = if (laneGuidance == LaneDirection.SHAPE_SHARP_RIGHT) {
                255
            } else {
                128
            }
            canvas.translate(-translateX.toFloat(), -marginY)
            index++

        }
        return bitmap
    }

    fun getDirectionImageForLaneDirection(lane: Int): Int {
        return when (lane) {
            LaneDirection.SHAPE_SHARP_RIGHT -> R.drawable.direction_white_turnright
            else -> R.drawable.direction_white_straight
        }
    }
}