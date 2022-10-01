package com.example.jetexpensesapp.components.shared

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

enum class ButtonVariants() {
    TEXT, OUTLINED, CONTAINED;
}

@Composable
fun Button(
    modifier: Modifier = Modifier,
    text: String?,
    shape: Shape = CircleShape,
    onClick: () -> Unit,
    contentColor: Color = Color.White,
    backgroundColor: Color = MaterialTheme.colors.primary,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    variant: ButtonVariants = ButtonVariants.CONTAINED
) {
    when (variant) {
        ButtonVariants.TEXT -> {
            OutlinedButton(
                onClick = onClick, shape = shape,
                border = BorderStroke(
                    0.dp,
                    brush = Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
                ),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent,
                    contentColor = contentColor,
                    disabledBackgroundColor = Color.Transparent,
                    disabledContentColor = contentColor.copy(alpha = ContentAlpha.disabled)
                ), enabled = enabled, modifier = modifier
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (icon != null) {
                        Icon(imageVector = icon, contentDescription = null)
                    }
                    if (text != null) {
                        Text(text, modifier = Modifier.padding(start = 3.dp))
                    }
                }
            }
        }
        ButtonVariants.CONTAINED -> {
            Button(
                onClick = onClick, shape = shape, colors = ButtonDefaults.buttonColors(
                    backgroundColor = backgroundColor,
                    contentColor = contentColor,
                    disabledBackgroundColor = Color.Transparent,
                    disabledContentColor = contentColor.copy(alpha = ContentAlpha.disabled)
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (icon != null) {
                        Icon(imageVector = icon, contentDescription = null)
                    }
                    if (text != null) {
                        Text(text, modifier = Modifier.padding(start = 3.dp))
                    }
                }
            }
        }
        ButtonVariants.OUTLINED -> {
            OutlinedButton(onClick = onClick) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (icon != null) {
                        Icon(imageVector = icon, contentDescription = null)
                    }
                    if (text != null) {
                        Text(text, modifier = Modifier.padding(start = 3.dp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun buttonPrev() {
    Button(text = "Test", onClick = { /*TODO*/ }, icon = Icons.Filled.Delete)
}