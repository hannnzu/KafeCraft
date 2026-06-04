package com.example.kafecraft.ui.profile

import android.R
import android.media.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
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
        HorizontalDivider(color = Color(0xFFF5E6E0))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
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
                Text("Posts", fontSize = 14.sp, color = Color.Gray)
            }
        }
        Box(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp), contentAlignment = Alignment.Center) {
            Box(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp), contentAlignment = Alignment.Center) {
                Text("My Bookmark", fontSize = 14.sp, color = Color(0xFFFF7A45), fontWeight = FontWeight.Medium)
            }

        }
        HorizontalDivider(color = Color(0xFFFF7A45))
        Spacer(Modifier.height(16.dp))
        if (bookmarks.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                Text("Belum ada resep y")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                items(bookmarks) { bookmark ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {onNavigateToDetail(bookmark.recipesId)},
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(1.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = bookmark.title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF332211)
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = null,
                                tint = Color(0xFFFF7A45),
                            )
                        }
                    }

                }


            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun ProfileContentPreview() {
    val dummyBookmarks = listOf(
        BookmarkEntity("1", "Nasi Goreng Pataya", "Nasi Goreng Dalam Telur", "Haik"),
        BookmarkEntity("2", "Nasi Goreng Mawut", "Nasi Goreng Diawut", "Haik")
    )
    MaterialTheme{
        ProfileContent(
            userName = "Budi",
            userHandle = "@budisantoso",
            postCount = 5,
            bookmarks = dummyBookmarks,
            onLogoutClick = {},
            onNavigateToDetail = {}
        )
    }
}