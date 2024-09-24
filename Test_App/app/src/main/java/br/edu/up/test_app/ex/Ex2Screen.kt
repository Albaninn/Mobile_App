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
fun Ex2(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Esta é o exercicio 2") },
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
            LayoutListas2() // Implementação do LayoutListas
        }
    }
}

@Composable
fun LayoutListas2() {
    val cores1alinha = listOf(Color.Green, Color.Red)
    val cores2alinha = listOf(Color.Yellow, Color.Blue)

    Column(modifier = Modifier.fillMaxSize()) {

        val modifierRow = Modifier
            .fillMaxWidth()
            .weight(1f)
        val modifierBox = Modifier
            .fillMaxHeight()
            .weight(1f)

        Row(modifier = modifierRow) {
            cores1alinha.forEach { cor ->
                Box(
                    modifier = modifierBox
                        .then(Modifier.background(cor))
                )
            }
        }
        Row(modifier = modifierRow) {
            cores2alinha.forEach { cor ->
                Box(
                    modifier = modifierBox
                        .then(Modifier.background(cor))
                )
            }
        }
    }
}