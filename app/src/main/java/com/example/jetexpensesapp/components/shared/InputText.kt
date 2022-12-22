package com.example.jetexpensesapp.components.shared

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RetirementInputText(
    modifier: Modifier = Modifier,
    text: String,
    label: String,
    readOnly: Boolean = false,
    maxLine: Int = 1,
    enabled: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation { transform ->
        TransformedText(
            transform,
            OffsetMapping.Identity
        )
    },
    interactionSource: MutableInteractionSource = MutableInteractionSource(),
    trailingIcon: @Composable () -> Unit = {},
    onTextChange: (String) -> Unit,
    onImeAction: () -> Unit = {}
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = text,
        onValueChange = onTextChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        label = { Text(text = label) },
        maxLines = maxLine,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = keyboardType
        ),
        keyboardActions = KeyboardActions {
            onImeAction()
            keyboardController?.hide()
        },
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        trailingIcon = trailingIcon,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent
        ),
    )
}