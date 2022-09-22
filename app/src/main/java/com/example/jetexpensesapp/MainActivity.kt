package com.example.jetexpensesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.jetexpensesapp.navigation.ExpensesNavigation
import com.example.jetexpensesapp.screen.udis.AddRetirementEntryScreen
import com.example.jetexpensesapp.ui.theme.JetExpensesAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetExpensesAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Scaffold {
//                        //UdiHome()
//                        AddRetirementEntryScreen()
                        ExpensesNavigation()
                    }
                }
            }
        }
    }
}


//@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetExpensesAppTheme {
    }
}