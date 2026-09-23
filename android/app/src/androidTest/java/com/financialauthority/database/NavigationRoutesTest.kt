package com.financialauthority.database

import androidx.activity.ComponentActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.testing.TestNavHostController
import com.financialauthority.database.navigation.Routes
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class NavigationRoutesTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun opensRequiredRoutes() {
        lateinit var navController: TestNavHostController

        composeTestRule.setContent {
            val context = LocalContext.current
            navController = TestNavHostController(context)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            NavHost(navController = navController, startDestination = Routes.COUNTRIES) {
                composable(Routes.COUNTRIES) {}
                composable(Routes.COUNTRY_DETAIL) {}
                composable(Routes.INTERNATIONAL) {}
                composable(Routes.INSTITUTION_DETAIL) {}
                composable(Routes.FSRBS) {}
                composable(Routes.FSRB_DETAIL) {}
                composable(Routes.FIUS) {}
                composable(Routes.FIU_DETAIL) {}
                composable(Routes.SEARCH) {}
                composable(Routes.FAVORITES) {}
                composable(Routes.ABOUT) {}
                composable(Routes.ABOUT_PRIVACY) {}
                composable(Routes.ABOUT_TERMS) {}
                composable(Routes.ABOUT_DISCLAIMER) {}
                composable(Routes.ABOUT_SOURCES) {}
                composable(Routes.ABOUT_LICENSES) {}
                composable(Routes.ABOUT_CONTACT) {}
            }
            LaunchedEffect(Unit) {
                navController.navigate("countries/italy")
                navController.navigate("international/fatf_gafi")
                navController.navigate("fsrbs/moneyval")
                navController.navigate("fius/it_uif")
                navController.navigate(Routes.ABOUT_PRIVACY)
                navController.navigate(Routes.ABOUT_TERMS)
                navController.navigate(Routes.ABOUT_DISCLAIMER)
            }
        }

        composeTestRule.runOnIdle {
            assertEquals(Routes.ABOUT_DISCLAIMER, navController.currentBackStackEntry?.destination?.route)
        }
    }
}
