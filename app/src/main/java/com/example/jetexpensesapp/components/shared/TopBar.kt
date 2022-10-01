package com.example.jetexpensesapp.components.shared

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun TopBar(
    navController: NavController? = null,
    title: String = "",
    buttonText: String = "",
    icon: ImageVector = Icons.Filled.ArrowBack,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.primary,
    elevation: Dp = 0.dp ,
    onClick: () -> Unit
) {
    TopAppBar(
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = { navController?.popBackStack() }) {
                Icon(imageVector = icon, contentDescription = null)
            }
        },
        title = { Text(title) },
        backgroundColor = backgroundColor,
        elevation = elevation,
        actions = {
            Button(
                text = buttonText,
                contentColor = MaterialTheme.colors.primary,
                variant = ButtonVariants.TEXT,
                onClick = onClick
            )
        }
    )
}