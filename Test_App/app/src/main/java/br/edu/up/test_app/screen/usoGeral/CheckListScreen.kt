package br.edu.up.test_app.screen.usoGeral

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckListScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Checklists") },
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())  // Permite rolagem
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            // Coluna para centralizar os botões
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp), // Espaçamento entre os botões
                modifier = Modifier.fillMaxWidth() // Garante que a coluna preencha a largura disponível
            ) {
                // Botão para "Utilização de Veículos"
                Button(
                    onClick = {
                        // Navega para a tela de Utilização de Veículos
                        navController.navigate("utilizacao_de_veiculos")
                    },
                    modifier = Modifier.fillMaxWidth(0.8f) // Definindo o tamanho fixo para o botão (80% da largura)
                ) {
                    Text("Utilização de Veículos")
                }

                // Outro botão para checklist futuro
                Button(
                    onClick = {
                        // Futuramente, navega para outra tela de checklist
                        // navController.navigate("outro_checklist")
                    },
                    modifier = Modifier.fillMaxWidth(0.8f) // Definindo o mesmo tamanho
                ) {
                    Text("EM BREVE")
                }
            }
        }
    }
}