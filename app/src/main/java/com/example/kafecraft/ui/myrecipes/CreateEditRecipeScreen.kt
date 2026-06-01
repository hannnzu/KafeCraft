package com.example.kafecraft.ui.myrecipes

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CreateEditRecipeScreen(
    recipeId: String,
    viewModel: MyRecipesViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val isEditMode = recipeId.isNotEmpty()
    val existingRecipe = if (isEditMode) viewModel.myRecipes.find { it.id == recipeId } else null

    var title by remember { mutableStateOf(existingRecipe?.recipe?.title ?: "") }
    var description by remember { mutableStateOf(existingRecipe?.recipe?.description ?: "") }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.saveMessage) {
        viewModel.saveMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.resetMessage()
            if (it.contains("berhasil")) onNavigateBack()
        }
    }

    CreateEditREcipeContent(
        isEditMode = isEditMode,
        title = title,
        onTitleChange = { title = it },
        description = description,
        onDescriptionChange = { description = it },
        isSaving = viewModel.isSaving,
        onDeleteClick = { showDeleteDialog = true },
        onSaveClick = {
            if (title.isBlank() || description.isBlank()) {
                Toast.makeText(context, "Harap isi nama dan deskripsi resep", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.saveRecipe(
                    title, description,
                    if (isEditMode) recipeId else null
                )
            }
        },
        onCancelClick = onNavigateBack
    )

    if (showDeleteDialog && existingRecipe != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Hapus Resep?", fontWeight = FontWeight.Bold) },
            text = { Text("Resep ini akan dihapus permanen dan tidak bisa dikembalikan.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteRecipe(recipeId)
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                ) {
                    Text("Hapus")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteDialog = false }) {
                    Text("Batal", color = Color(0xFF332211))
                }
            },
            containerColor = Color.White
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEditREcipeContent(
    isEditMode: Boolean,
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    isSaving: Boolean,
    onDeleteClick: () -> Unit,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit
) {

}

@Preview()
@Composable
fun CreateEditRecipeScreenPreview() {

}