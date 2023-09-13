package bme.projlab.rikiki

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.filters.LargeTest
import androidx.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class AuthenticationTest{

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun loginButtonExistsAndClickable() {
        composeTestRule.apply {
            onNodeWithTag("welcomeLoginButton")
                .assertIsDisplayed()
                .performClick()
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun loginIncorrectly() {
        composeTestRule.apply {
            onNodeWithTag("welcomeLoginButton")
                .assertIsDisplayed()
                .performClick()
            onNodeWithTag("emailField")
                .assertIsDisplayed()
                .performTextInput("notCorrectEmail")
            onNodeWithTag("passwordField")
                .assertIsDisplayed()
                .performTextInput("notCorrectPassword")
            onNodeWithTag("loginButton")
                .assertIsDisplayed()
                .performClick()
            waitUntilExactlyOneExists(
                hasText("Loading"),1000
            )
            waitUntilExactlyOneExists(
                hasText("Error message"),6000
            )
        }
    }
}