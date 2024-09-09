package br.edu.up.test_app.ex

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import br.edu.up.test_app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tela dos Exercícios") },
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Texto no topo
            Text(
                text = "Esta é a Tela dos Exercícios",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .border(2.dp, Color.White)
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botões organizados em duas colunas
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { navController.navigate("ex_1") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                    ) {
                        Text("Ir para exercicio 1")
                    }

                    Button(
                        onClick = { navController.navigate("ex_2") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                    ) {
                        Text("Ir para exercicio 2")
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { navController.navigate("ex_3") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                    ) {
                        Text("Ir para exercicio 3")
                    }

                    Button(
                        onClick = { navController.navigate("ex_4") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                    ) {
                        Text("Ir para exercicio 4")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Ex1(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Esta é o exercicio 1") },
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
            LayoutListas1() // Implementação do LayoutListas
        }
    }
}

@Composable
fun LayoutListas1() {
    val coresalinha = listOf(Color.Green, Color.Red,Color.Yellow, Color.Blue)

    Row(modifier = Modifier.fillMaxSize()) {

        val modifierColumn = Modifier
            .fillMaxHeight()
            .weight(1f)
        val modifierBox = Modifier
            .fillMaxWidth()
            .weight(1f)

        Column(modifier = modifierColumn) {
            coresalinha.forEach { cor ->
                Box(
                    modifier = modifierBox
                        .then(Modifier.background(cor))
                )
            }
        }
    }
}

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
