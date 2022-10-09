package com.example.jetexpensesapp.components.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.jetexpensesapp.R

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
    shouldDisplayFilter: Boolean = false,
    onFilterNew: () -> Unit = {},
    onFilterLast: () -> Unit = {},
    onBack: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    TopAppBar(
        modifier = modifier,
        navigationIcon = {
            if (icon != null) {
                IconButton(onClick = onBack) {
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
            if (shouldDisplayFilter)
                FilterDatesMenu(onFilterNew = onFilterNew, onFilterLast)
        }
    )
}

@Composable
private fun FilterDatesMenu(
    onFilterNew: () -> Unit,
    onFilterLast: () -> Unit
) {
    TopBarDropdownMenu(iconContent = {
        Icon(
            Icons.Filled.List,
            null
        )
    }) { closeMenu ->
        DropdownMenuItem(onClick = { onFilterNew(); closeMenu() }) {
            Text(text = stringResource(id = R.string.label_new))
        }
        DropdownMenuItem(onClick = { onFilterLast(); closeMenu() }) {
            Text(text = stringResource(id = R.string.label_old))
        }
    }
}

@Composable
private fun TopBarDropdownMenu(
    iconContent: @Composable () -> Unit,
    content: @Composable ColumnScope.(() -> Unit) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {
        IconButton(onClick = { expanded = !expanded }) {
            iconContent()
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.wrapContentSize(Alignment.TopEnd)
        ) {
            content { expanded = !expanded }
        }
    }
}