import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerificacaoVeiculosScreen(navController: NavHostController) {
    var placa by remember { mutableStateOf("") }
    var modelo by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Vistoria do Veículo") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = placa,
                onValueChange = { placa = it },
                label = { Text("Placa do Veículo") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = modelo,
                onValueChange = { modelo = it },
                label = { Text("Modelo do Veículo") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    if (placa.isNotBlank() && modelo.isNotBlank()) {
                        // Navegar para UtilizacaoVeiculosScreen passando Placa e Modelo
                        navController.navigate("utilizacao_de_veiculos/$placa/$modelo")
                    } else {
                        // Mostra um erro ou notificação, se necessário
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Continuar para Verificação do Veículo")
            }
        }
    }
}
