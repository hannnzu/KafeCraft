package com.example.kafecraft.ui.auth

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ForgotPasswordScreen(
    viewModel: AuthViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(viewModel.isSuccess, viewModel.error) {
        if (viewModel.isSuccess){
            Toast.makeText(context, "Link Reset Password Telah Dikirm", Toast.LENGTH_SHORT).show()
            viewModel.resetState()
            onNavigateBack()
        }
        viewModel.error?.let{
            Toast.makeText(context,it, Toast.LENGTH_SHORT).show()
            viewModel.resetState()
        }
    }
    ForgotPasswordContent(
        isLoading = viewModel.isLoading,
        onSendReset = {email -> viewModel.sendPassworReset(email )},
        onNavigateBack = onNavigateBack
    )
}
@Composable
private fun ForgotPasswordContent(
    isLoading: Boolean = false,
    onSendReset: (String) -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
        Icons.Default.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier.align(Alignment.Start).size(24.dp).clickable{onNavigateBack}
    )
        Spacer(Modifier.height(32.dp))
        Text("Lupa Password?")
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = email,
            onValueChange = {email = it},
            label = { Text("Enail")},
            placeholder = {Text("nama@email.com")},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFF7A45),
                unfocusedBorderColor = Color(0xFFE0E0E0)
            )
        )
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = {
                if (email.isEmpty()){
                    Toast.makeText(context,"Harap isi Email", Toast.LENGTH_SHORT).show()
                } else {
                    onSendReset(email)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7A45)),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Kirim Link Reset", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(Modifier.height(16.dp))
        TextButton(onClick = {onNavigateBack()}) {
            Text("Kembali ke Login", color = Color(0xFFFF7A45), fontWeight = FontWeight.Bold)
        }
    }
}
@Preview(showBackground = true)
@Composable
private fun ForgotPasswordScreenPreview(){
    ForgotPasswordContent()
}