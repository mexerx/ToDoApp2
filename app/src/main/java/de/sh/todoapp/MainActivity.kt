package de.sh.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import de.sh.todoapp.ui.screen.Dashboard
import de.sh.todoapp.ui.theme.ToDoAppTheme

/**
 * Main activity for the ToDo app.
 *
 * @constructor Create empty Main activity for the ToDo app.
 */
class MainActivity : ComponentActivity() {

    /**
     * Called when the activity is starting.
     *
     * @param savedInstanceState The previously saved instance state, if any.
     */
    override fun onCreate(savedInstanceState: Bundle?) {

        // Call the superclass implementation of onCreate to perform default initialization.
        super.onCreate(savedInstanceState)

        // Set the content of the activity to the Dashboard composable function.
        setContent {
            ToDoAppTheme {
                Dashboard() // Start the dashboard screen when the activity is created and ready to be displayed.
            }
        }
    }
}
