
package com.example.projectappmovil

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.projectappmovil.controller.CommentController
import com.example.projectappmovil.navegation.AppScreens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Comments() {
    val auth: FirebaseAuth = Firebase.auth
    val user = auth.currentUser
    val userId = user?.uid
    val db = Firebase.firestore
    Scaffold(
        bottomBar = {
            NavigationBar {
                var comentario by remember { mutableStateOf("") }
                var nombre by remember { mutableStateOf("") }

                LaunchedEffect(user?.email) {
                    db.collection("clientes")
                        .whereEqualTo("email", user?.email)
                        .get()
                        .addOnSuccessListener { querySnapshot ->
                            if (!querySnapshot.isEmpty){
                                val document = querySnapshot.documents[0]
                                nombre = document.getString("nombre") ?: ""
                            }
                        }
                }

                Row (
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    TextField(
                        value = comentario,
                        onValueChange = {comentario = it},
                        label = { Text("Comentar")},
                        modifier = Modifier
                            .background(color = Color.Transparent)
                            .border(1.dp, color = MaterialTheme.colorScheme.primary)
                            .fillMaxWidth(0.8f)
                            .height(50.dp),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Create,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = Color.White
                            )
                        }
                    )
                    val save = CommentController()
                    IconButton(
                        onClick = {save.saveComment(nombre, comentario, userId!!) },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.White)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(25.dp),
                            tint = Color.Black
                        )
                    }
                }

            }
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Comentarios",
                    fontSize = 20.sp
                ) },
                navigationIcon = {
                    Image(
                        painter = painterResource(R.drawable.vueloenavion),
                        contentDescription = null,
                        modifier = Modifier.size(50.dp)
                    )
                },
                actions = {
                    SmallFloatingActionButton (
                        onClick = { },
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.DarkGray)
            )
        }
    ) { innerPadding ->
        LoadComments(innerPadding)
    }
}

@Composable
fun LoadComments(innerPadding: PaddingValues) {
    val db = Firebase.firestore
    var comment by remember { mutableStateOf<List<Comments>>(emptyList()) }

    LaunchedEffect(Unit) {
        val listenerRegistration = db.collection("comentarios")
            .addSnapshotListener { snapshot: QuerySnapshot?, exception: FirebaseFirestoreException? ->
                if (exception != null) {
                    println("Error listening to Firestore: ${exception.message}")
                    return@addSnapshotListener
                }
                val newComment = snapshot?.documents?.mapNotNull { document ->
                    Comments(
                        nombre = document.getString("nombre") ?: "",
                        descripcion = document.getString("descripcion") ?: "",
                        userId = document.getString("userId") ?: ""
                    )
                } ?: emptyList()

                comment = newComment
            }
    }
    MyLazyColumn3(comments = comment, innerPadding)
}

data class Comments(
    val nombre: String,
    val descripcion: String,
    val userId: String
)

@Composable
fun MyLazyColumn3(comments: List<Comments>, innerPadding: PaddingValues) {
    LazyColumn(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {
        items(comments) { comment ->
            Card(
                modifier = Modifier
                    .padding(vertical = 5.dp)
                    .border(1.dp, Color.White, shape = MaterialTheme.shapes.medium)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(Color.Gray)

            ) {
                Column {
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = MaterialTheme.colorScheme.primary)
                            .padding(5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(25.dp),
                            tint = Color.Black,
                        )
                        Text(
                            text = "By: ${comment.nombre}",
                            fontSize = 15.sp,
                            modifier = Modifier.padding(horizontal = 5.dp),
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }

                    Text(text = comment.descripcion,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp))
                }

                }
        }
    }
}





