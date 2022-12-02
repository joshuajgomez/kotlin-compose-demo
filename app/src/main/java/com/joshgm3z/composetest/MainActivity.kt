@file:OptIn(ExperimentalMaterial3Api::class)

package com.joshgm3z.composetest

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.joshgm3z.composetest.ui.theme.ComposeTestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Screen()
                }
            }
        }
    }
}

data class Profile(val name: String, val age: Int)

class DemoViewModel : ViewModel() {

    var profileList = mutableStateListOf<Profile>()

    fun onAddClick(name: String) {
        profileList.add(Profile(name, 56))
        Log.i("JoshDemo", "onAddClick: $name, profileList.size=${profileList.size}")
    }

    fun onDeleteClick(profile: Profile) {
        profileList.remove(profile)
        Log.i("JoshDemo", "onDeleteClick: ${profile.name}, profileList.size=${profileList.size}")
    }

    fun onClearAllClick() {
        profileList.clear()
        Log.i("JoshDemo", "onClearAllClick profileList.size=${profileList.size}")
    }
}

@Composable
fun Screen(demoViewModel: DemoViewModel = viewModel()) {
    Content(
        profileList = demoViewModel.profileList,
        onAddClick = { demoViewModel.onAddClick(it) },
        onDeleteClick = { demoViewModel.onDeleteClick(it) },
        onClearAllClick = { demoViewModel.onClearAllClick() },
    )
}

@Composable
fun Content(
    profileList: MutableList<Profile>,
    onAddClick: (String) -> Unit,
    onDeleteClick: (Profile) -> Unit,
    onClearAllClick: () -> Unit,
) {
    Column(modifier = Modifier.padding(all = 10.dp)) {
        var name by remember { mutableStateOf("") }

        AnimatedVisibility(visible = name.isEmpty()) {
            Text(text = "Enter your name")
        }
        AnimatedVisibility(visible = name.isNotEmpty()) {
            Text(text = "Hello $name")
        }

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(text = "Type a name") }
        )

        Row {
            Button(onClick = { onAddClick(name) }) {
                Text(text = "add")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(onClick = { name = "" }) {
                Text(text = "clear")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(onClick = { onClearAllClick() }) {
                Text(text = "clear all profiles")
            }
        }

        ProfileList(profileList = profileList, onDeleteClick = onDeleteClick)
    }
}

@Composable
fun ProfileList(profileList: MutableList<Profile>, onDeleteClick: (Profile) -> Unit) {
    AnimatedVisibility(visible = profileList.isEmpty()) {
        Text(text = "No profiles found")
    }
    LazyColumn {
        items(items = profileList) {
            ProfileItem(profile = it) {
                onDeleteClick(it)
            }
        }
    }
}

@Composable
fun ProfileItem(profile: Profile, onDeleteClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Outlined.Delete,
            contentDescription = "delete icon",
            modifier = Modifier.clickable { onDeleteClick() }
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(text = "Name: ${profile.name}")
        Spacer(modifier = Modifier.width(20.dp))
        Text(text = "Age: ${profile.age}")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeTestTheme {
        Screen()
    }
}