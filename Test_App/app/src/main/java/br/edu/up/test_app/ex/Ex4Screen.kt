package br.edu.up.test_app.ex

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import br.edu.up.test_app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Ex4(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Esta é o exercicio 4") },
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
            LayoutListas4() // Implementação do LayoutListas4
        }
    }
}

@Preview
@Composable
fun LayoutListas4() {
    val cores1alinha = listOf(Color.Green, Color.Red)
    val cores2alinha = listOf(Color.Blue, Color.Yellow)
    val cores3alinha = listOf(Color.Red, Color.Green)

    val cores5alinha = listOf(Color.Red, Color.Yellow)
    val cores6alinha = listOf(Color.Green, Color.Blue)

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val modifierRow = Modifier
            .fillMaxWidth()
            .weight(1f)
        val modifierBox = Modifier
            .fillMaxHeight()
            .weight(1f)
        // Primeira linha: Imagem à esquerda, cores à direita
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile_icon),
                    contentDescription = "Foto de Perfil",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
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

        // Segunda linha: Cores à esquerda, imagem à direita
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Row(modifier = modifierRow) {
                    cores3alinha.forEach { cor ->
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
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile_icon),
                    contentDescription = "Foto de Perfil",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }
        }

        // Terceira linha: Imagem à esquerda, cores à direita
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile_icon),
                    contentDescription = "Foto de Perfil",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Row(modifier = modifierRow) {
                    cores5alinha.forEach { cor ->
                        Box(
                            modifier = modifierBox
                                .then(Modifier.background(cor))
                        )
                    }
                }
                Row(modifier = modifierRow) {
                    cores6alinha.forEach { cor ->
                        Box(
                            modifier = modifierBox
                                .then(Modifier.background(cor))
                        )
                    }
                }
            }
        }
    }
}