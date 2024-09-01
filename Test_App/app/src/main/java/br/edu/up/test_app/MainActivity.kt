package br.edu.up.test_app

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.edu.up.test_app.ui.theme.Test_AppTheme
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyApp() {
    // Detecta o tema do sistema na primeira inicialização
    val isSystemDarkTheme = isSystemInDarkTheme()
    var isDarkTheme by remember { mutableStateOf(isSystemDarkTheme) }

    Test_AppTheme(darkTheme = isDarkTheme) {
        val navController = rememberNavController()
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            // Configuração da Navegação
            NavigationComponent(navController, isDarkTheme, onThemeChange = { isDarkTheme = it })
        }
    }
}


// Mapa de usuários com nome completo, senha e email
val userCredentials = mutableMapOf(
    "l.serenato" to UserProfile("Lucas Albano Ribas Serenato", "1234", "lk.serenato@example.com","adm"),
    "lorenna.j" to UserProfile("Lorenna Judit", "qwe123", "lohve@example.com","user"),
    "cesar.a" to UserProfile("Cesar Augusto Serenato", "senha", "cesar@example.com","coord"),
    "mirian.a" to UserProfile("Mirian Albano Ribas", "senha", "mirian@example.com","user")
)

// Data class para armazenar as informações do perfil
data class UserProfile(
    val fullName: String,
    val password: String,
    val email: String,
    val accessLevel: String
)

// Mapa de horas trabalhadas por usuário
@RequiresApi(Build.VERSION_CODES.O)
val workHours = mutableMapOf(
    "l.serenato" to mutableListOf(
        WorkDay("16/08/2024", "07:48", "18:02"),
        WorkDay("14/08/2024", "07:58", "18:00"),
        WorkDay("13/08/2024", "07:38", "17:20"),
        WorkDay("12/08/2024", "07:45", "18:00"),
        WorkDay("15/08/2024", "08:00", "12:00")
    ),
    "lorenna.j" to mutableListOf(
        WorkDay("16/08/2024", "08:15", "17:30")
    )
)

// Composable para navegação
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationComponent(navController: NavHostController, isDarkTheme: Boolean, onThemeChange: (Boolean) -> Unit) {
    NavHost(navController = navController, startDestination = "registration_screen") {
        composable("registration_screen") { RegistrationScreen(navController) }
        composable("login_screen") { LoginScreen(navController) }
        composable(
            route = "second_screen/{username}",
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: "Desconhecido"
            MainScreen(navController, username, isDarkTheme = isDarkTheme, onThemeChange)
        }
        composable("profile_screen/{username}",
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: "Desconhecido"
            ProfileScreen(navController, username)
        }
        composable("tela_1") { Tela1Screen(navController) }
        composable(
            route = "tela_2/{username}",
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: "Desconhecido"
            Tela2Screen(navController, username)
        }
        composable("tela_3") { Tela3Screen(navController) }
    }
}

@Composable
fun RegistrationScreen(navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var accessLevel by remember { mutableStateOf("user") }
    var isRegistering by remember { mutableStateOf(false) } // Controle para exibir campos de cadastro
    var errorMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Bem-vindo", style = MaterialTheme.typography.headlineLarge)

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de nome de usuário
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Usuário") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de senha
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Senha") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            // Mostrar os campos de nome completo e email ao clicar no botão de cadastro
            if (isRegistering) {
                Spacer(modifier = Modifier.height(16.dp))

                // Campo de nome completo
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Nome Completo") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo de email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mensagem de erro
            if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Botões lado a lado
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        if (isRegistering) {
                            // Valida os campos de cadastro
                            when {
                                fullName.isEmpty() -> errorMessage = "O campo nome completo não pode estar vazio."
                                email.isEmpty() -> errorMessage = "O campo email não pode estar vazio."
                                username.isEmpty() -> errorMessage = "O campo usuário não pode estar vazio."
                                password.isEmpty() -> errorMessage = "O campo senha não pode estar vazio."
                                else -> {
                                    errorMessage = ""
                                    // Adicionar o usuário à base de dados (simulação)
                                    userCredentials[username] = UserProfile(fullName, password, email, accessLevel)
                                    navController.navigate("second_screen/$username")
                                }
                            }
                        } else {
                            // Validação para login
                            when {
                                username.isEmpty() -> errorMessage = "O campo usuário não pode estar vazio."
                                password.isEmpty() -> errorMessage = "O campo senha não pode estar vazio."
                                userCredentials.containsKey(username) && userCredentials[username]?.password == password -> {
                                    errorMessage = ""
                                    // Navegar para a segunda tela passando o nome do usuário
                                    navController.navigate("second_screen/$username")
                                }
                                else -> errorMessage = "Usuário ou senha inválidos."
                            }
                        }
                    },
                    modifier = Modifier.weight(1f) // Ocupa metade da linha
                ) {
                    Text(text = if (isRegistering) "Cadastrar" else "Entrar")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = { isRegistering = !isRegistering }, // Alterna o modo de cadastro
                    modifier = Modifier.weight(1f) // Ocupa metade da linha
                ) {
                    Text(text = if (isRegistering) "Cancelar" else "Cadastro")
                }
            }
        }
    }
}

// Tela de Login
@Composable
fun LoginScreen(navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Login", style = MaterialTheme.typography.headlineLarge)

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de nome de usuário
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Usuário") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de senha
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Senha") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Mensagem de erro
            if (loginError) {
                Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Botão de login
            Button(
                onClick = {
                    // Valida os campos
                    if (username.isEmpty()) {
                        loginError = true
                        errorMessage = "O campo usuário não pode estar vazio."
                    } else if (password.isEmpty()) {
                        loginError = true
                        errorMessage = "O campo senha não pode estar vazio."
                    } else if (userCredentials.containsKey(username) && userCredentials[username]?.password == password) {
                        loginError = false
                        // Navegar para a segunda tela passando o nome do usuário
                        navController.navigate("second_screen/$username")
                    } else {
                        loginError = true
                        errorMessage = "Usuário ou senha inválidos."
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Entrar")
            }
        }
    }
}

// Tela Principal (onde está o Card)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController, username: String, isDarkTheme: Boolean, onThemeChange: (Boolean) -> Unit) {
    // Buscar as informações do usuário a partir do mapa userCredentials
    val userProfile = userCredentials[username] ?: UserProfile("Desconhecido", "", "", "")

    // Extrair o primeiro nome do fullName
    val firstName = userProfile.fullName.split(" ").firstOrNull() ?: "Desconhecido"

    // Estado para controlar a abertura do drawer
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    // Itens do drawer
    val items = mutableListOf("Consulta de Hora", "Tela 3") // Inicialmente só incluem as telas acessíveis a todos

    // Adicionar "Tela 1" se o nível de acesso for "adm"
    if (userProfile.accessLevel == "adm") {
        items.add(0, "Tela administrativa") // Adiciona "Tela 1" ao topo da lista
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(16.dp))
                Text("Opções", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(16.dp))

                items.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item) },
                        onClick = {
                            // Abrir a tela correspondente
                            when (item) {
                                "Tela administrativa" -> navController.navigate("tela_1")
                                "Consulta de Hora" -> navController.navigate("tela_2/$username")
                                "Tela 3" -> navController.navigate("tela_3")
                            }
                            coroutineScope.launch { drawerState.close() } // Fecha o drawer após o clique
                        }
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Página Inicial") },
                    navigationIcon = {
                        IconButton(onClick = {
                            // Abrir o drawer ao clicar no ícone do menu
                            coroutineScope.launch { drawerState.open() }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu, // Ícone de menu padrão
                                contentDescription = "Abrir Menu"
                            )
                        }
                    },
                    actions = {
                        // Ícone do perfil que, ao ser clicado, abre o DropdownMenu
                        Box {
                            var showMenu by remember { mutableStateOf(false) }

                            IconButton(onClick = { showMenu = !showMenu }) {
                                // Aqui substituí o ic_profile pela imagem do usuário logado (profile_icon)
                                Image(
                                    painter = painterResource(id = R.drawable.profile_icon),
                                    contentDescription = "Perfil",
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                )
                            }

                            // DropdownMenu que aparece quando o ícone de perfil é clicado
                            DropdownMenu(
                                expanded = showMenu,
                                onDismissRequest = { showMenu = false } // Fechar menu quando clicar fora
                            ) {
                                // Opção "Gerenciar Perfil"
                                DropdownMenuItem(
                                    text = { Text("Gerenciar Perfil") },
                                    onClick = {
                                        // Navegar para a página de perfil
                                        navController.navigate("profile_screen/$username")
                                        showMenu = false // Fechar o menu
                                    }
                                )

                                // Adiciona um Spacer para separar as opções anteriores
                                Spacer(Modifier.height(8.dp))

                                // Adiciona o switch para alternar o tema
                                DropdownMenuItem(
                                    text = {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(text = "Modo Escuro", style = MaterialTheme.typography.bodySmall) // Texto menor
                                            Spacer(modifier = Modifier.weight(1f))
                                            Switch(
                                                checked = isDarkTheme,
                                                onCheckedChange = { onThemeChange(it) },
                                                modifier = Modifier.scale(0.7f) // Escala o switch para 70% do tamanho original
                                            )
                                        }
                                    },
                                    onClick = {} // Não faz nada ao clicar, já que o Switch é o controle principal
                                )

                                // Opção "Trocar Conta"
                                DropdownMenuItem(
                                    text = { Text("Trocar Conta", style = MaterialTheme.typography.bodySmall) }, // Texto menor
                                    onClick = {
                                        navController.navigate("login_screen")
                                        showMenu = false // Fechar o menu
                                    }
                                )

                                // Opção "Sair da Conta" - Agora redireciona para a tela de cadastro
                                DropdownMenuItem(
                                    text = { Text("Sair da Conta", style = MaterialTheme.typography.bodySmall) }, // Texto menor
                                    onClick = {
                                        navController.navigate("registration_screen")
                                        showMenu = false // Fechar o menu
                                    }
                                )
                            }
                        }
                    }
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                // Exibe o cartão com a mensagem de boas-vindas
                Card(Mensagem(firstName, "Olá, seja bem-vindo!"))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Tela1Screen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tela 1") },
                navigationIcon = {
                    IconButton(onClick = {
                        // Voltar para a tela anterior (SecondScreen)
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
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text("Esta é a Tela 1", style = MaterialTheme.typography.headlineLarge)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Tela2Screen(navController: NavHostController, username: String) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Consulta Banco de Horas") },
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
                .padding(padding)
        ) {
            // Exibir a lista de registros de banco de horas do usuário logado
            WorkHoursList(username = username)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WorkHoursList(username: String) {
    // Filtra e ordena os registros para o usuário logado por data, do mais recente para o mais antigo
    val workDays = workHours[username]?.sortedByDescending {
        LocalDate.parse(it.date, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    } ?: emptyList()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        workDays.forEach { workDay ->
            WorkDayItem(workDay)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WorkDayItem(workDay: WorkDay) {
    // Calcula as situações (horas totais ajustadas, crédito, débito)
    val totalHours = workDay.totalAdjustedHours // Total de horas ajustado (-1h para almoço)
    val credit = workDay.credit
    val debit = workDay.debit

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Primeira Coluna: Data e dia da semana
        Column(
            modifier = Modifier
                .weight(2f) // Aumentado para 2f para dar mais espaço
                .align(Alignment.CenterVertically) // Centraliza o conteúdo verticalmente
        ) {
            Text(
                text = workDay.date,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally) // Centraliza o texto horizontalmente
            )
            Text(
                text = workDay.dayOfWeek,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.CenterHorizontally) // Centraliza o texto horizontalmente
            )
        }

        // Segunda Coluna: Marcações (Entrada e Saída)
        Column(
            modifier = Modifier
                .weight(1.5f) // Reduzido para 1.5f
                .align(Alignment.CenterVertically) // Centraliza o conteúdo verticalmente
        ) {
            Text(
                text = "${workDay.entryTime} - ${workDay.exitTime}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally) // Centraliza o texto horizontalmente
            )
        }

        // Terceira Coluna: Situações (Trabalhadas, Crédito, Débito)
        Column(
            modifier = Modifier
                .weight(2f)
                .align(Alignment.CenterVertically) // Centraliza o conteúdo verticalmente
        ) {
            Text(
                text = "$totalHours Trabalhado",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally) // Centraliza o texto horizontalmente
            )
            Text(
                text = if (credit != "00:00") "$credit Crédito" else "$debit Débito",
                style = MaterialTheme.typography.bodyMedium,
                color = if (credit != "00:00") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.CenterHorizontally) // Centraliza o texto horizontalmente
            )
        }
    }

    Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
}

// Modificações na data class WorkDay para ajustar as horas de trabalho e calcular o crédito/débito:
@RequiresApi(Build.VERSION_CODES.O)
data class WorkDay(
    val date: String,
    val entryTime: String,
    val exitTime: String
) {
    val dayOfWeek: String = calculateDayOfWeek(date)

    // Calcula as horas totais ajustadas (-1h para almoço)
    @RequiresApi(Build.VERSION_CODES.O)
    val totalAdjustedHours: String = calculateTotalAdjustedHours(entryTime, exitTime)

    // Considera 9 horas como jornada padrão de segunda a quinta e 8 horas na sexta
    @RequiresApi(Build.VERSION_CODES.O)
    val credit: String = calculateCredit(totalAdjustedHours)
    @RequiresApi(Build.VERSION_CODES.O)
    val debit: String = calculateDebit(totalAdjustedHours)

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateTotalAdjustedHours(entryTime: String, exitTime: String): String {
        val entry = LocalTime.parse(entryTime)
        val exit = LocalTime.parse(exitTime)
        val totalMinutes = ChronoUnit.MINUTES.between(entry, exit) - 60 // Subtrai 60 minutos (1 hora de almoço)
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return "%02d:%02d".format(hours, minutes)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateCredit(totalHours: String): String {
        val total = LocalTime.parse(totalHours)
        val standardHours = if (dayOfWeek == "sexta-feira") LocalTime.of(8, 0) else LocalTime.of(9, 0)
        return if (total.isAfter(standardHours)) {
            val extraMinutes = ChronoUnit.MINUTES.between(standardHours, total)
            val hours = extraMinutes / 60
            val minutes = extraMinutes % 60
            "%02d:%02d".format(hours, minutes)
        } else {
            "00:00"
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateDebit(totalHours: String): String {
        val total = LocalTime.parse(totalHours)
        val standardHours = if (dayOfWeek == "sexta-feira") LocalTime.of(8, 0) else LocalTime.of(9, 0)
        return if (total.isBefore(standardHours)) {
            val deficitMinutes = ChronoUnit.MINUTES.between(total, standardHours)
            val hours = deficitMinutes / 60
            val minutes = deficitMinutes % 60
            "%02d:%02d".format(hours, minutes)
        } else {
            "00:00"
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateDayOfWeek(date: String): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val localDate = LocalDate.parse(date, formatter)
        return localDate.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.getDefault())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Tela3Screen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tela 3") },
                navigationIcon = {
                    IconButton(onClick = {
                        // Voltar para a tela anterior (SecondScreen)
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
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text("Esta é a Tela 3", style = MaterialTheme.typography.headlineLarge)
        }
    }
}

// Tela de Perfil do Usuário
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavHostController, username: String) {
    // Buscar as informações do usuário a partir do mapa userCredentials
    val userProfile = userCredentials[username] ?: UserProfile("Desconhecido", "", "", "")

    // Extrair o primeiro nome do fullName
    val firstName = userProfile.fullName.split(" ").firstOrNull() ?: "Desconhecido"

    // Estado para controlar se o perfil está em modo de edição
    var isEditing by remember { mutableStateOf(false) }

    // Estado local para os campos de texto
    var fullName by remember { mutableStateOf(userProfile.fullName) }
    var email by remember { mutableStateOf(userProfile.email) }
    var password by remember { mutableStateOf(userProfile.password) }
    val accessLevel = userProfile.accessLevel // Nível de acesso é sempre não editável

    // Cores customizadas para os campos desabilitados
    val customTextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        disabledTextColor = MaterialTheme.colorScheme.onSurface, // Cor do texto quando desabilitado
        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant, // Cor do rótulo quando desabilitado
        disabledBorderColor = MaterialTheme.colorScheme.onSurface // Cor da borda quando desabilitado
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil de $firstName") }, // Mostra o primeiro nome
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack() // Voltar para a tela anterior
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack, // Ícone de seta para voltar
                            contentDescription = "Voltar"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        isEditing = !isEditing // Alternar o modo de edição
                    }) {
                        Icon(
                            imageVector = Icons.Default.Edit, // Ícone de edição
                            contentDescription = if (isEditing) "Salvar" else "Editar Perfil"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp), // Espaçamento entre os elementos
                modifier = Modifier.padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile_icon),
                    contentDescription = "Foto de Perfil",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )

                // Campo de nome completo
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Nome Completo") },
                    enabled = isEditing, // Habilita edição apenas se isEditing for true
                    colors = customTextFieldColors, // Define as cores customizadas
                    modifier = Modifier.fillMaxWidth()
                )

                // Campo de nome de usuário (não editável)
                OutlinedTextField(
                    value = username,
                    onValueChange = {},
                    label = { Text("Nome de Usuário") },
                    enabled = false,
                    colors = customTextFieldColors, // Define as cores customizadas
                    modifier = Modifier.fillMaxWidth()
                )

                // Campo de email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    enabled = isEditing, // Habilita edição apenas se isEditing for true
                    colors = customTextFieldColors, // Define as cores customizadas
                    modifier = Modifier.fillMaxWidth()
                )

                // Campo de senha
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Senha") },
                    enabled = isEditing, // Habilita edição apenas se isEditing for true
                    visualTransformation = PasswordVisualTransformation(),
                    colors = customTextFieldColors, // Define as cores customizadas
                    modifier = Modifier.fillMaxWidth()
                )

                // Campo de nível de acesso (não editável)
                OutlinedTextField(
                    value = accessLevel,
                    onValueChange = {},
                    label = { Text("Nível de Acesso") },
                    enabled = false, // Não pode ser editado
                    colors = customTextFieldColors, // Define as cores customizadas
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

data class Mensagem(val autor: String, val texto: String)

@Composable
private fun Card(msg: Mensagem) {
    Row {
        Image(
            painter = painterResource(id = R.drawable.profile_icon),
            contentDescription = "Foto",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = msg.texto)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = msg.autor)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    Test_AppTheme {
        LoginScreen(navController = rememberNavController())
    }
}


