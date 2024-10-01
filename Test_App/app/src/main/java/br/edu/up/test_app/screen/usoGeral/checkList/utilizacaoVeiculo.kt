package br.edu.up.test_app.screen.usoGeral.checkList

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import br.edu.up.test_app.R

// Modelo básico de uma seção de checklist
data class ChecklistSection(
    val title: String,
    val questions: List<String>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UtilizacaoVeiculosScreen(navController: NavHostController) {
    // Definir as 5 seções obrigatórias que podem ser expandidas/recolhidas
    val expandableSections = listOf(
        ChecklistSection("Seção 1: Verificação de Luzes", listOf("Farol Dianteiro", "Setas Dianteiras", "Farol Traseiro", "Setas Traseiras", "Luz de Freio")),
        ChecklistSection("Seção 2: Verificação da Mecânica", listOf("Nível de Óleo do motor", "Nível de Água", "Nível de água da Bateria", "Pneus Dianteiros", "Pneus Traseiros", "Estepe", "Macaco", "Chave de Roda", "Triângulo de sinalização")),
        ChecklistSection("Seção 3: Verificação de Lataria", listOf("Frontal", "Lateral Direita", "Traseira", "Lateral Esquerda", "Teto")),
        ChecklistSection("Seção 4: Verificação de Vidros", listOf("Para-brisa", "Janelas Lateral Direita", "Vigia", "Janela Lateral Esquerda")),
        ChecklistSection("Seção 5: Verificação de Documentos", listOf("Documento CRLV", "Manual do Proprietário"))
    )

    // Usar estado para armazenar a seleção de "Aprovado" ou "Reprovado" para cada item (somente para seções obrigatórias)
    val approvalStates = remember { mutableStateListOf(*Array(expandableSections.flatMap { it.questions }.size) { -1 }) }

    // Usar estado para armazenar se a seção está expandida ou não
    val expandedStates = remember { mutableStateListOf(*Array(expandableSections.size) { false }) }

    // Verificar se todas as perguntas das seções obrigatórias foram respondidas (Seções 1 a 5)
    val allItemsAnswered = approvalStates.all { it != -1 } // Apenas as perguntas obrigatórias

    // Variável para controlar o estado do diálogo de confirmação
    var showDialog by remember { mutableStateOf(false) }
    var showIncompleteDialog by remember { mutableStateOf(false) }

    // Launcher para tirar foto
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) {
            // Lógica para lidar com a imagem tirada
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Utilização de Veículos") },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Outras seções obrigatórias (seções 1 a 5)
            expandableSections.forEachIndexed { sectionIndex, section ->
                item {
                    var hasReprovado by remember { mutableStateOf(false) }

                    // Cabeçalho da seção com funcionalidade de expandir/encolher
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                expandedStates[sectionIndex] = !expandedStates[sectionIndex]
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = section.title,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Icon(
                            painter = painterResource(id = if (expandedStates[sectionIndex]) R.drawable.arrowup else R.drawable.arrowdown),
                            contentDescription = if (expandedStates[sectionIndex]) "Recolher" else "Expandir"
                        )
                    }

                    // Verificar se a seção está expandida
                    if (expandedStates[sectionIndex]) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val globalIndexOffset = sectionsTakeTotalQuestions(sectionIndex, expandableSections)

                            section.questions.forEachIndexed { questionIndex, question ->
                                val globalIndex = globalIndexOffset + questionIndex

                                ChecklistItemWithApproval(
                                    question = question,
                                    selectedOption = approvalStates[globalIndex],
                                    onOptionSelected = { selectedOption ->
                                        approvalStates[globalIndex] = selectedOption
                                    },
                                    imageBitmap = null,
                                    onTakePictureClicked = {
                                        launcher.launch(null)
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Botão para concluir checklist
            item {
                Button(
                    onClick = {
                        if (!allItemsAnswered) {
                            showIncompleteDialog = true // Exibir o diálogo se o checklist estiver incompleto
                        } else {
                            showDialog = true // Exibir o diálogo de checklist completo
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Concluir Checklist")
                }
            }
        }
    }

    // Exibir o diálogo de confirmação após concluir (checklist completo)
    if (showDialog) {
        ConfirmationDialog(
            title = "Checklist Completo",
            message = "O checklist foi preenchido e enviado com sucesso. Deseja fazer o download do documento?",
            onDismiss = { showDialog = false },
            confirmButtonLabel = "Download",
            dismissButtonLabel = "Não",
            onConfirm = {
                showDialog = false
                // Lógica de download
            }
        )
    }

    // Exibir o diálogo de checklist incompleto
    if (showIncompleteDialog) {
        ConfirmationDialog(
            title = "Checklist Incompleto",
            message = "O checklist ainda não está completo. Deseja continuar assim mesmo?",
            onDismiss = { showIncompleteDialog = false },
            confirmButtonLabel = "Continuar",
            dismissButtonLabel = "Cancelar",
            onConfirm = {
                showIncompleteDialog = false
                // Lógica de continuar mesmo com checklist incompleto
            }
        )
    }
}

// Função para calcular a posição global dos índices
fun sectionsTakeTotalQuestions(take: Int, sections: List<ChecklistSection>): Int {
    return sections.take(take).sumOf { it.questions.size }
}

@Composable
fun ChecklistItemWithApproval(
    question: String,
    selectedOption: Int,
    onOptionSelected: (Int) -> Unit,
    imageBitmap: Bitmap?,
    onTakePictureClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Texto da pergunta ocupando uma parte da linha
        Text(
            text = question,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        )

        // Opção de Aprovado e Reprovado
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Aprovado", style = MaterialTheme.typography.bodyMedium)
            RadioButton(
                selected = selectedOption == 0,
                onClick = { onOptionSelected(0) },
                colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 16.dp)) {
            Text(text = "Reprovado", style = MaterialTheme.typography.bodyMedium)
            RadioButton(
                selected = selectedOption == 1,
                onClick = { onOptionSelected(1) },
                colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
            )
        }
    }

    // Se o item for reprovado, exibir o botão para incluir imagem
    if (selectedOption == 1) {
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onTakePictureClicked) {
            Text("Incluir Imagem para $question Reprovado")  // Exibe o nome do item reprovado
        }

        // Mostrar pré-visualização da imagem, se disponível
        imageBitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Imagem de $question reprovado",
                modifier = Modifier.size(100.dp).padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
    confirmButtonLabel: String,
    dismissButtonLabel: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(confirmButtonLabel)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(dismissButtonLabel)
            }
        }
    )
}