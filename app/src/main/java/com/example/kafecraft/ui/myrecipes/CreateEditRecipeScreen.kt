package com.example.kafecraft.ui.myrecipes

import android.widget.Toast
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview

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

    CreateEditREcipeContent()

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEditREcipeContent(

) {

}

@Preview()
@Composable
fun CreateEditRecipeScreenPreview() {

}