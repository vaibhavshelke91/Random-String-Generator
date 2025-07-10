package com.vaibhav.randomstringgenerator

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModelProvider

import androidx.lifecycle.viewmodel.compose.viewModel
import com.vaibhav.randomstringgenerator.data.model.RandomString

import com.vaibhav.randomstringgenerator.data.repository.StringRepository
import com.vaibhav.randomstringgenerator.ui.theme.RandomStringGeneratorTheme
import com.vaibhav.randomstringgenerator.utils.UiState

import com.vaibhav.randomstringgenerator.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RandomStringGeneratorTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val context = LocalContext.current
    val viewModel = viewModel<MainViewModel>()
    val strings = viewModel.randomString.collectAsState().value

    var isDialogOpen by remember {
        mutableStateOf(false)
    }

    var isLoadingDialogVisible by remember {
        mutableStateOf(false)
    }

    val tempList = remember {
        mutableStateListOf<RandomString>()
    }

    LaunchedEffect(key1 = strings) {
       when(strings){
           is UiState.Error -> {
               Toast.makeText(context,strings?.message, Toast.LENGTH_SHORT).show()
               isLoadingDialogVisible=false
           }
           UiState.Idle -> {
               isLoadingDialogVisible=false
           }
           UiState.Loading -> {
               isLoadingDialogVisible=true
           }
           is UiState.Success -> {
               tempList.add(strings.data)
               isLoadingDialogVisible=false

           }
       }
    }


    InputDialog(showDialog = isDialogOpen, onDismiss = { isDialogOpen=false}) { len->
        viewModel.generateByLength(len.toInt())
        isDialogOpen=false
    }

    LoadingDialog(isVisible = isLoadingDialogVisible)


    
    Scaffold(modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = {
                isDialogOpen=true
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "")
                Text(text = "Add New")
            }
        },
        topBar = {
            TopAppBar(title = { Text(text = "Random String Generator") })
        }) { innerPadding ->

        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(innerPadding)){

            if (tempList.isEmpty()){
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp), contentAlignment = Alignment.Center){
                    Text(
                        textAlign = TextAlign.Center,
                       color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                        text = "No String generated please click + Button to generate new random string")
                }

            }else{
                LazyColumn(modifier = Modifier.fillMaxSize()) {


                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Generated",
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )

                            Button(onClick = { tempList.clear() }) {
                                Text("Delete All")
                            }
                        }
                    }

                    items(tempList.reversed()) {
                        RandomTextCard(it){item->
                            tempList.remove(item)
                        }
                    }
                }

            }


        }

    }
}


@Composable
fun RandomTextCard(randomString: RandomString,onDeleteClick : (RandomString) -> Unit) {
    val model = randomString.randomText

    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
       
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
            Text(
                text = "${model?.value}",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Length: ${model?.length}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = "Created: ${model?.created}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.padding(top = 5.dp))
            Button(onClick = {
                onDeleteClick(randomString)
            }, modifier = Modifier.align(Alignment.End)) {
                Text(text = "Delete")
            }
        }
    }
}


@Composable
fun InputDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    if (showDialog) {
        var text by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(text = "Length")
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = text,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        onValueChange = { text = it },
                        placeholder = { Text("Enter Length") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { onConfirm(text) }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        )
    }
}


@Composable
fun LoadingDialog(isVisible: Boolean) {
    if (isVisible) {
        Dialog(onDismissRequest = {}) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(12.dp)
                    )
                ,
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RandomStringGeneratorTheme {
        MainScreen()
    }
}