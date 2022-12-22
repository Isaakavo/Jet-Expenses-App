package com.example.jetexpensesapp.components.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import com.example.jetexpensesapp.R

@Composable
fun EditOptions(
    onHideSheet: () -> Unit,
    onDeleteOption: () -> Unit,
    onEditOption: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            text = "Borrar",
            modifier = Modifier.weight(0.5f),
            shape = RectangleShape,
            contentColor = colorResource(R.color.error),
            icon = Icons.Filled.Delete,
            variant = ButtonVariants.TEXT,
            onClick = {
                onDeleteOption()
                onHideSheet()
                //viewModel.deleteUdi(retirementPlan = retirementData.value)
            })
        Button(
            text = "Editar",
            modifier = Modifier.weight(0.5f),
            shape = RectangleShape,
            contentColor = colorResource(R.color.accepted),
            icon = Icons.Filled.Edit,
            variant = ButtonVariants.TEXT,
            onClick = {
                onEditOption()
                onHideSheet()
            })
    }
}