package br.edu.up.test_app.screen.usoGeral.checkList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

// Modelo básico de uma seção de checklist
data class ChecklistSection(
    val title: String,
    val questions: List<String>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UtilizacaoVeiculosScreen(navController: NavHostController) {
    // Definir as 7 seções do checklist
    val sections = listOf(
        ChecklistSection("Seção Veículo: ", listOf("Placa: ", "Modelo do veículo: ")),
        ChecklistSection("Seção 1: Verificação de Luzes", listOf("Farol Dianteiro", "Setas Dianteiras", "Farol Traseiro", "Setas Traseiras", "Luz de Freio")),
        ChecklistSection("Seção 2: Verificação dos Mecânica", listOf("Nível de Óleo do motor", "Nível de Água", "Nível de água da Bateria", "Pneus Dianteiros", "Pneus Traseiros", "Estepe", "Macaco", "Chave de Roda", "Triângulo de sinalização")),
        ChecklistSection("Seção 3: Verificação de Lataria", listOf("Frontal", "Lateral Direita", "Traseira", "Lateral Esquerda", "Teto")),
        ChecklistSection("Seção 4: Verificação de Vidros", listOf("Para-brisa", "Janelas Lateral Direita", "Vigia", "Janela Lateral Esquerda")),
        ChecklistSection("Seção 5: Verificação de Documentos", listOf("Documento CRLV", "Manual do Proprietário")),
        ChecklistSection("Seção 6: Observações", listOf("Observações"))
    )

    // Usar estado para armazenar a seleção de "Aprovado" ou "Reprovado" para cada item
    val approvalStates = remember { mutableStateListOf(*Array(sections.flatMap { it.questions }.size) { -1 }) }

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
            // Iterar pelas seções do checklist
            sections.forEachIndexed { sectionIndex, section ->
                item {
                    // Título da seção
                    Text(
                        text = section.title,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Iterar pelas perguntas da seção
                        section.questions.forEachIndexed { questionIndex, question ->
                            val globalIndex = sections.take(sectionIndex).sumOf { it.questions.size } + questionIndex
                            ChecklistItemWithApproval(
                                question = question,
                                selectedOption = approvalStates[globalIndex],
                                onOptionSelected = { selectedOption ->
                                    approvalStates[globalIndex] = selectedOption
                                }
                            )
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
                .weight(1f)  // O texto da pergunta ocupa o máximo de espaço disponível
                .padding(end = 8.dp)  // Adiciona um espaçamento entre o texto e as opções
        )

        // Opção de Aprovado e Reprovado, alinhadas à direita
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Aprovado", style = MaterialTheme.typography.bodyMedium)
            RadioButton(
                selected = selectedOption == 0,
                onClick = { onOptionSelected(0) },
                colors = RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colorScheme.primary
                )
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Text(text = "Reprovado", style = MaterialTheme.typography.bodyMedium)
            RadioButton(
                selected = selectedOption == 1,
                onClick = { onOptionSelected(1) },
                colors = RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}