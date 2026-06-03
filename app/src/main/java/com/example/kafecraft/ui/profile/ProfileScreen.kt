package com.example.kafecraft.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kafecraft.data.BookmarkEntity
import com.example.kafecraft.data.SessionManager
import com.example.kafecraft.ui.home.HomeViewModel
import com.example.kafecraft.ui.myrecipes.MyRecipesViewModel

@Composable
fun ProfileScreen(
    sessionManager: SessionManager,
    homeViewModel: HomeViewModel,
    myRecipesViewModel: MyRecipesViewModel,
    onLogout: () -> Unit,
    onNavigateToDetail: (String) -> Unit
) {
    val bookmarks by homeViewModel.bookmarks.collectAsState()
    val userName = sessionManager.getUserName() ?: "User"
    val userHandle = "@${userName.lowercase().replace("","")}"

    ProfileContent(
        userName = userName,
        userHandle = userHandle,
        postCount = myRecipesViewModel.myRecipes.size,
        bookmarks = bookmarks,
        onLogoutClick = onLogout,
        onNavigateToDetail = onNavigateToDetail
    )
}

@Composable
fun ProfileContent(
    userName: String,
    userHandle: String,
    postCount: Int,
    bookmarks: List<BookmarkEntity>,
    onLogoutClick: () -> Unit,
    onNavigateToDetail: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Profile", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF332211))
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable{ onLogoutClick() }
            ) {
                Icon(Icons.Outlined.ExitToApp, contentDescription = "logout", tint = Color(0xFFD32F2F), modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(4.dp))
                Text("Keluar", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD32F2F))
            }
        }
        HorizontalDivider(color = Color(0xFFD32F2F))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, bottom = 16.dp)
        ) {
            Box(
                contentAlignment = Alignment.BottomEnd,
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFE0D5CB)), contentAlignment = Alignment.Center){
                        Text(userName.take(1).uppercase(), fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF7A45))
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            Text(userName, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF332211))
            Spacer(Modifier.height(4.dp))
            Text(userHandle, fontSize = 14.sp, color = Color(0xFFFF7A45))
            Spacer(Modifier.height(24.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(postCount.toString(), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF332211))
                Text("Post", fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
}