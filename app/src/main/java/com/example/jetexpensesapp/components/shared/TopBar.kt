package com.example.jetexpensesapp.components.shared

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun TopBar(
    //navController: NavController? = null,
    title: String = "",
    titleModifier: Modifier = Modifier,
    titleStyle: TextStyle = MaterialTheme.typography.h6,
    titleWeight: FontWeight = FontWeight.Normal,
    buttonText: String = "",
    icon: ImageVector? = Icons.Filled.ArrowBack,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.primary,
    elevation: Dp = 0.dp,
    navControllerAction: () -> Unit = {},
    onClick: () -> Unit
) {
    TopAppBar(
        modifier = modifier,
        navigationIcon = {
            if (icon != null) {
                IconButton(onClick = navControllerAction) {
                    Icon(imageVector = icon, contentDescription = null)
                }
            }
        },
        title = {
            Text(
                title,
                modifier = titleModifier,
                style = titleStyle,
                fontWeight = titleWeight
            )
        },
        backgroundColor = backgroundColor,
        elevation = elevation,
        actions = {
            if (buttonText.isNotEmpty()) {
                Button(
                    text = buttonText,
                    contentColor = MaterialTheme.colors.primary,
                    variant = ButtonVariants.TEXT,
                    onClick = onClick
                )
            }

        }
    )
}