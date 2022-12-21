package com.example.jetexpensesapp.components.shared

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

enum class ButtonVariants() {
    TEXT, OUTLINED, CONTAINED;
}

@Composable
fun Button(
    modifier: Modifier = Modifier.animateContentSize(),
    text: String? = null,
    shape: Shape = CircleShape,
    onClick: () -> Unit,
    contentColor: Color = Color.White,
    backgroundColor: Color = MaterialTheme.colors.primary,
    icon: ImageVector? = null,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    variant: ButtonVariants = ButtonVariants.CONTAINED
) {
    when (variant) {
        ButtonVariants.TEXT -> {
            OutlinedButton(
                onClick = onClick,
                shape = shape,
                border = BorderStroke(
                    0.dp,
                    brush = Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
                ),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent,
                    contentColor = contentColor,
                    disabledBackgroundColor = Color.LightGray,
                    disabledContentColor = contentColor.copy(alpha = ContentAlpha.disabled)
                ),
                enabled = !isLoading,
                modifier = modifier
            ) {
                ButtonContent(text, icon, isLoading)
            }
        }
        ButtonVariants.CONTAINED -> {
            Button(
                onClick = onClick,
                shape = shape,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = backgroundColor,
                    contentColor = contentColor,
                    disabledBackgroundColor = Color.LightGray,
                    disabledContentColor = contentColor.copy(alpha = ContentAlpha.disabled)
                ),
                enabled = !isLoading,
                modifier = modifier
            ) {
                ButtonContent(text, icon, isLoading)
            }
        }
        ButtonVariants.OUTLINED -> {
            OutlinedButton(onClick = onClick, enabled = !isLoading) {
                ButtonContent(text, icon, isLoading)
            }
        }
    }
}

@Composable
fun ButtonContent(
    text: String? = null,
    icon: ImageVector? = null,
    isLoading: Boolean = false,
) {
    if (isLoading && icon == null) {
        CircularProgressIndicator(
            color = White,
            strokeWidth = 2.dp,
            modifier = Modifier.size(15.dp)
        )
    }
    if (icon != null) {
        if (isLoading) {
            CircularProgressIndicator()
            if (text != null) {
                Text(text, modifier = Modifier.padding(start = 3.dp))
            }
        }
        Icon(imageVector = icon, contentDescription = null)
    }
    if (text != null) {
        Text(text, modifier = Modifier.padding(start = if (isLoading) 8.dp else 0.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun buttonPrev() {
    Button(
        text = "Login",
        onClick = { /*TODO*/ },
        icon = null,
        isLoading = true,
        variant = ButtonVariants.CONTAINED
    )
}