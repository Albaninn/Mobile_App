package br.edu.up.test_app.ex

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Ex3(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Esta é o exercicio 3") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LayoutListas3() // Implementação do LayoutListas3
        }
    }
}

@Composable
fun LayoutListas3() {
    // Lista de cores para cada linha
    val linhas = listOf(
        listOf(Color.Red, Color.Red, Color.Red, Color.Green),    // Linha 1
        listOf(Color.Red, Color.Red, Color.Blue, Color.Yellow),  // Linha 2
        listOf(Color.Red, Color.Green, Color.Yellow, Color.Yellow), // Linha 3
        listOf(Color.Blue, Color.Yellow, Color.Yellow, Color.Yellow), // Linha 4
        listOf(Color.Green, Color.Green, Color.Red, Color.Yellow), // Linha 5
        listOf(Color.Green, Color.Green, Color.Green, Color.Blue) // Linha 6
    )

    Column(modifier = Modifier.fillMaxSize()) {
        linhas.forEach { coresLinha ->
            Row(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)) {
                coresLinha.forEach { cor ->
                    Box(modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(cor))
                }
            }
        }
    }
}