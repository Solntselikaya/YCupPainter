package com.velikanova.ycuppainter.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.autofill.AutofillCallback.register
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHost
import androidx.navigation.NavHostController

private const val ANIMATION_SPEED = 250

@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: String
) {
    val startFeature: StartFeature = koinInject()
    val licenseFeature: LicenseFeature = koinInject()
    val projectFeature: ProjectFeature = koinInject()
    val roadFeature: RoadFeature = koinInject()
    val distanceMarkFeature: DistanceMarkFeature = koinInject()

    val exportFeature: ExportFeature = koinInject()
    val busStationFeature: BusStationFeature = koinInject()
    val pipesFeature: PipesFeature = koinInject()
    val bridgeFeature: BridgeFeature = koinInject()
    val tunnelFeature: TunnelFeature = koinInject()
    val galleryFeature: GalleryFeature = koinInject()
    val serviceObjectFeature: ServiceObjectFeature = koinInject()

    val settingFeature: SettingFeature = koinInject()
    val surveyFeature: SurveyFeature = koinInject()
    val templateFeature: TemplateFeature = koinInject()
    val abstractMarkFeature: AbstractMarkFeature = koinInject()
    val filterFeature: FilterFeature = koinInject()
    val photoFeature: PhotoFeature = koinInject()

    val mapFeature: MapFeature = koinInject()
    val kmlFeature: KmlFeature = koinInject()
    val advertismentFeature: AdvertismentFeature = koinInject()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { fadeIn(animationSpec = tween(ANIMATION_SPEED)) },
        exitTransition = { fadeOut(animationSpec = tween(ANIMATION_SPEED)) }
    ) {
        register(
            featureApi = startFeature,
            navController = navController
        )

        register(
            featureApi = licenseFeature,
            navController = navController
        )

        register(
            featureApi = mapFeature,
            navController = navController
        )

        register(
            featureApi = projectFeature,
            navController = navController
        )

        register(
            featureApi = roadFeature,
            navController = navController
        )

        register(
            featureApi = exportFeature,
            navController = navController
        )

        register(
            featureApi = pipesFeature,
            navController = navController
        )

        register(
            featureApi = bridgeFeature,
            navController = navController,
        )

        register(
            featureApi = settingFeature,
            navController = navController
        )

        register(
            featureApi = surveyFeature,
            navController = navController
        )

        register(
            featureApi = templateFeature,
            navController = navController
        )

        register(
            featureApi = abstractMarkFeature,
            navController = navController
        )

        register(
            featureApi = filterFeature,
            navController = navController
        )

        register(
            featureApi = photoFeature,
            navController = navController
        )

        register(
            featureApi = distanceMarkFeature,
            navController = navController
        )

        register(
            featureApi = kmlFeature,
            navController = navController
        )
        register(
            featureApi = busStationFeature,
            navController = navController,
        )
        register(
            featureApi = serviceObjectFeature,
            navController = navController,
        )

        register(
            featureApi = tunnelFeature,
            navController = navController
        )

        register(
            featureApi = galleryFeature,
            navController = navController
        )

        register(
            featureApi = advertismentFeature,
            navController = navController
        )
    }
}

fun NavGraphBuilder.register(
    featureApi: FeatureApi,
    navController: NavHostController,
) {
    featureApi.registerGraph(
        navGraphBuilder = this,
        navController = navController,
    )
}