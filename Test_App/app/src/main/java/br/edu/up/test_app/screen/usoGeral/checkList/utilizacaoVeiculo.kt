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
    // Definir as 6 seções que podem ser expandidas/recolhidas
    val expandableSections = listOf(
        ChecklistSection("Seção 1: Verificação de Luzes", listOf("Farol Dianteiro", "Setas Dianteiras", "Farol Traseiro", "Setas Traseiras", "Luz de Freio")),
        ChecklistSection("Seção 2: Verificação da Mecânica", listOf("Nível de Óleo do motor", "Nível de Água", "Nível de água da Bateria", "Pneus Dianteiros", "Pneus Traseiros", "Estepe", "Macaco", "Chave de Roda", "Triângulo de sinalização")),
        ChecklistSection("Seção 3: Verificação de Lataria", listOf("Frontal", "Lateral Direita", "Traseira", "Lateral Esquerda", "Teto")),
        ChecklistSection("Seção 4: Verificação de Vidros", listOf("Para-brisa", "Janelas Lateral Direita", "Vigia", "Janela Lateral Esquerda")),
        ChecklistSection("Seção 5: Verificação de Documentos", listOf("Documento CRLV", "Manual do Proprietário")),
        ChecklistSection("Seção 6: Observações", listOf("Observações"))
    )

    // "Seção Veículo" que deve sempre ficar visível
    val vehicleSection = ChecklistSection("Seção Veículo: ", listOf("Placa", "Modelo do veículo"))

    // Usar estado para armazenar a seleção de "Aprovado" ou "Reprovado" para cada item
    val approvalStates = remember { mutableStateListOf(*Array(expandableSections.flatMap { it.questions }.size) { -1 }) }

    // Usar estado para armazenar se a seção está expandida ou não
    val expandedStates = remember { mutableStateListOf(*Array(expandableSections.size) { false }) }

    // Estados para armazenar o valor da placa, modelo e observações
    var plateText by remember { mutableStateOf("") }
    var modelText by remember { mutableStateOf("") }
    var observationsText by remember { mutableStateOf("") }

    // Lista de imagens associadas às reprovações
    val reprovadoImages = remember { mutableStateListOf<Bitmap?>(null, null, null, null, null) }

    // Verificar se todas as perguntas foram respondidas
    val allAnswered = approvalStates.all { it != -1 }

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
            // Seção "Veículo" sempre visível com campos de texto para Placa e Modelo
            item {
                Text(
                    text = vehicleSection.title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = plateText,
                        onValueChange = { plateText = it },
                        label = { Text("Placa") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = modelText,
                        onValueChange = { modelText = it },
                        label = { Text("Modelo do Veículo") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Outras seções que podem ser expandidas/recolhidas
            expandableSections.forEachIndexed { sectionIndex, section ->
                item {
                    var hasReprovado by remember { mutableStateOf(false) } // Estado para verificar se há algum "Reprovado"

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
                                        // Verifica se alguma opção foi marcada como "Reprovado"
                                        if (selectedOption == 1) {
                                            hasReprovado = true
                                            reprovadoImages.add(null) // Preparar para associar uma imagem
                                        } else {
                                            hasReprovado = false
                                        }
                                    }
                                )
                            }

                            // Mostrar opção de upload de imagem para cada item reprovado
                            section.questions.forEachIndexed { questionIndex, question ->
                                val globalIndex = globalIndexOffset + questionIndex
                                if (approvalStates[globalIndex] == 1) { // Se o item foi reprovado
                                    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
                                        if (bitmap != null) {
                                            // Associar a imagem à reprovação correspondente
                                            reprovadoImages[globalIndex] = bitmap
                                        }
                                    }

                                    Button(onClick = { launcher.launch(null) }) {
                                        Text("Incluir Imagem do Item Reprovado")
                                    }

                                    // Mostrar pré-visualização da imagem, se disponível
                                    reprovadoImages[globalIndex]?.let {
                                        Image(bitmap = it.asImageBitmap(), contentDescription = "Imagem do item reprovado", modifier = Modifier.size(100.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item {
                // Botão para concluir checklist
                Button(
                    onClick = {
                        // Lógica para enviar ou salvar o checklist
                    },
                    enabled = allAnswered,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Concluir Checklist")
                }
            }
        }
    }
}

fun sectionsTakeTotalQuestions(take: Int, sections: List<ChecklistSection>): Int {
    return sections.take(take).sumOf { it.questions.size }
}

@Composable
fun ChecklistItemWithApproval(
    question: String,
    selectedOption: Int,
    onOptionSelected: (Int) -> Unit
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
}