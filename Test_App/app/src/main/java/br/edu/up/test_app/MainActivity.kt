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
    val isSystemDarkTheme = isSystemInDarkTheme()
    var isDarkTheme by remember { mutableStateOf(isSystemDarkTheme) }

    Test_AppTheme(darkTheme = isDarkTheme) {
        val navController = rememberNavController()
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NavigationComponent(navController, isDarkTheme, onThemeChange = { isDarkTheme = it })
        }
    }
}

val userCredentials = mutableMapOf(
    "l.serenato" to UserProfile("Lucas Albano Ribas Serenato", "1234", "lk.serenato@example.com","adm"),
    "lorenna.j" to UserProfile("Lorenna Judit", "qwe123", "lohve@example.com","user"),
    "cesar.a" to UserProfile("Cesar Augusto Serenato", "senha", "cesar@example.com","coord"),
    "mirian.a" to UserProfile("Mirian Albano Ribas", "senha", "mirian@example.com","user")
)

data class UserProfile(
    val fullName: String,
    val password: String,
    val email: String,
    val accessLevel: String
)

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
    var isRegistering by remember { mutableStateOf(false) }
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

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Usuário") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Senha") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            if (isRegistering) {
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Nome Completo") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        if (isRegistering) {
                            when {
                                fullName.isEmpty() -> errorMessage = "O campo nome completo não pode estar vazio."
                                email.isEmpty() -> errorMessage = "O campo email não pode estar vazio."
                                username.isEmpty() -> errorMessage = "O campo usuário não pode estar vazio."
                                password.isEmpty() -> errorMessage = "O campo senha não pode estar vazio."
                                else -> {
                                    errorMessage = ""
                                    userCredentials[username] = UserProfile(fullName, password, email, accessLevel)
                                    navController.navigate("second_screen/$username")
                                }
                            }
                        } else {
                            when {
                                username.isEmpty() -> errorMessage = "O campo usuário não pode estar vazio."
                                password.isEmpty() -> errorMessage = "O campo senha não pode estar vazio."
                                userCredentials.containsKey(username) && userCredentials[username]?.password == password -> {
                                    errorMessage = ""
                                    navController.navigate("second_screen/$username")
                                }
                                else -> errorMessage = "Usuário ou senha inválidos."
                            }
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = if (isRegistering) "Cadastrar" else "Entrar")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = { isRegistering = !isRegistering },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = if (isRegistering) "Cancelar" else "Cadastro")
                }
            }
        }
    }
}

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

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Usuário") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Senha") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (loginError) {
                Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    if (username.isEmpty()) {
                        loginError = true
                        errorMessage = "O campo usuário não pode estar vazio."
                    } else if (password.isEmpty()) {
                        loginError = true
                        errorMessage = "O campo senha não pode estar vazio."
                    } else if (userCredentials.containsKey(username) && userCredentials[username]?.password == password) {
                        loginError = false
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController, username: String, isDarkTheme: Boolean, onThemeChange: (Boolean) -> Unit) {
    val userProfile = userCredentials[username] ?: UserProfile("Desconhecido", "", "", "")

    val firstName = userProfile.fullName.split(" ").firstOrNull() ?: "Desconhecido"

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    val items = mutableListOf("Consulta de Hora", "Tela 3")

    if (userProfile.accessLevel == "adm") {
        items.add(0, "Tela administrativa")
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
                            when (item) {
                                "Tela administrativa" -> navController.navigate("tela_1")
                                "Consulta de Hora" -> navController.navigate("tela_2/$username")
                                "Tela 3" -> navController.navigate("tela_3")
                            }
                            coroutineScope.launch { drawerState.close() }
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
                            coroutineScope.launch { drawerState.open() }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Abrir Menu"
                            )
                        }
                    },
                    actions = {
                        Box {
                            var showMenu by remember { mutableStateOf(false) }

                            IconButton(onClick = { showMenu = !showMenu }) {
                                Image(
                                    painter = painterResource(id = R.drawable.profile_icon),
                                    contentDescription = "Perfil",
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                )
                            }

                            DropdownMenu(
                                expanded = showMenu,
                                onDismissRequest = { showMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Gerenciar Perfil") },
                                    onClick = {
                                        navController.navigate("profile_screen/$username")
                                        showMenu = false
                                    }
                                )

                                Spacer(Modifier.height(8.dp))

                                DropdownMenuItem(
                                    text = {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(text = "Modo Escuro", style = MaterialTheme.typography.bodySmall)
                                            Spacer(modifier = Modifier.weight(1f))
                                            Switch(
                                                checked = isDarkTheme,
                                                onCheckedChange = { onThemeChange(it) },
                                                modifier = Modifier.scale(0.7f)
                                            )
                                        }
                                    },
                                    onClick = {}
                                )

                                DropdownMenuItem(
                                    text = { Text("Trocar Conta", style = MaterialTheme.typography.bodySmall) },
                                    onClick = {
                                        navController.navigate("login_screen")
                                        showMenu = false
                                    }
                                )

                                DropdownMenuItem(
                                    text = { Text("Sair da Conta", style = MaterialTheme.typography.bodySmall) },
                                    onClick = {
                                        navController.navigate("registration_screen")
                                        showMenu = false
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

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Tela2Screen(navController: NavHostController, username: String) {
    var isEditing by remember { mutableStateOf(false) }

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
                },
                actions = {
                    IconButton(onClick = { isEditing = !isEditing }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = if (isEditing) "Salvar" else "Editar"
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
            WorkHoursList(username = username, isEditing = isEditing)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WorkHoursList(username: String, isEditing: Boolean) {
    val workDays = workHours[username]?.sortedByDescending {
        LocalDate.parse(it.date, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    } ?: emptyList()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        workDays.forEach { workDay ->
            WorkDayItem(workDay, isEditing)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WorkDayItem(workDay: WorkDay, isEditing: Boolean) {
    var entryTime by remember { mutableStateOf(workDay.entryTime) }
    var exitTime by remember { mutableStateOf(workDay.exitTime) }

    // Verifique e valide os horários antes de usá-los para cálculos
    val totalHours = tryCalculateTotalAdjustedHours(entryTime, exitTime, workDay.dayOfWeek)
    val credit = tryCalculateCredit(totalHours, workDay.dayOfWeek)
    val debit = tryCalculateDebit(totalHours, workDay.dayOfWeek)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Primeira Coluna: Data e dia da semana
        Column(
            modifier = Modifier
                .weight(2f)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = workDay.date,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = workDay.dayOfWeek,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        // Segunda Coluna: Marcações (Entrada e Saída)
        Column(
            modifier = Modifier
                .weight(1.5f)
                .align(Alignment.CenterVertically)
        ) {
            if (isEditing) {
                OutlinedTextField(
                    value = entryTime,
                    onValueChange = { entryTime = it },
                    label = { Text("Entrada") },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                OutlinedTextField(
                    value = exitTime,
                    onValueChange = { exitTime = it },
                    label = { Text("Saída") },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                Text(
                    text = "$entryTime - $exitTime",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }

        // Terceira Coluna: Situações (Horas Trabalhadas, Crédito, Débito)
        Column(
            modifier = Modifier
                .weight(2f)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = "$totalHours Trabalhado",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = if (credit != "00:00") "$credit Crédito" else "$debit Débito",
                style = MaterialTheme.typography.bodyMedium,
                color = if (credit != "00:00") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }

    Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
}

// Funções auxiliares para cálculo com tratamento de exceções

@RequiresApi(Build.VERSION_CODES.O)
fun tryCalculateTotalAdjustedHours(entryTime: String, exitTime: String, dayOfWeek: String): String {
    return try {
        calculateTotalAdjustedHours(entryTime, exitTime, dayOfWeek)
    } catch (e: Exception) {
        "Erro"
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun tryCalculateCredit(totalHours: String, dayOfWeek: String): String {
    return try {
        calculateCredit(totalHours, dayOfWeek)
    } catch (e: Exception) {
        "Erro"
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun tryCalculateDebit(totalHours: String, dayOfWeek: String): String {
    return try {
        calculateDebit(totalHours, dayOfWeek)
    } catch (e: Exception) {
        "Erro"
    }
}

@RequiresApi(Build.VERSION_CODES.O)
data class WorkDay(
    val date: String,
    var entryTime: String,
    var exitTime: String
) {
    val dayOfWeek: String = calculateDayOfWeek(date)

    @RequiresApi(Build.VERSION_CODES.O)
    val totalAdjustedHours: String = calculateTotalAdjustedHours(entryTime, exitTime, dayOfWeek)

    @RequiresApi(Build.VERSION_CODES.O)
    val credit: String = calculateCredit(totalAdjustedHours, dayOfWeek)
    @RequiresApi(Build.VERSION_CODES.O)
    val debit: String = calculateDebit(totalAdjustedHours, dayOfWeek)
}

@RequiresApi(Build.VERSION_CODES.O)
fun calculateTotalAdjustedHours(entryTime: String, exitTime: String, dayOfWeek: String): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    val entry = LocalTime.parse(entryTime, formatter)
    val exit = if (exitTime.isBlank()) LocalTime.of(12, 0) else LocalTime.parse(exitTime, formatter)

    var totalMinutes = ChronoUnit.MINUTES.between(entry, exit)

    if (exit >= LocalTime.of(13, 0)) {
        totalMinutes -= 60
    }

    totalMinutes = if (totalMinutes < 0) 0 else totalMinutes

    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60
    return "%02d:%02d".format(hours, minutes)
}

@RequiresApi(Build.VERSION_CODES.O)
fun calculateCredit(totalHours: String, dayOfWeek: String): String {
    val total = parseTime(totalHours)
    val standardHours = if (dayOfWeek.equals("sexta-feira", ignoreCase = true)) LocalTime.of(8, 0) else LocalTime.of(9, 0)
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
fun calculateDebit(totalHours: String, dayOfWeek: String): String {
    val total = parseTime(totalHours)
    val standardHours = if (dayOfWeek.equals("sexta-feira", ignoreCase = true)) LocalTime.of(8, 0) else LocalTime.of(9, 0)
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
fun calculateDayOfWeek(date: String): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val localDate = LocalDate.parse(date, formatter)
    return localDate.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.getDefault())
}

@RequiresApi(Build.VERSION_CODES.O)
private fun parseTime(time: String): LocalTime {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return LocalTime.parse(time, formatter)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavHostController, username: String) {
    val userProfile = userCredentials[username] ?: UserProfile("Desconhecido", "", "", "")

    val firstName = userProfile.fullName.split(" ").firstOrNull() ?: "Desconhecido"

    var isEditing by remember { mutableStateOf(false) }

    var fullName by remember { mutableStateOf(userProfile.fullName) }
    var email by remember { mutableStateOf(userProfile.email) }
    var password by remember { mutableStateOf(userProfile.password) }
    val accessLevel = userProfile.accessLevel

    val customTextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        disabledTextColor = MaterialTheme.colorScheme.onSurface,
        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledBorderColor = MaterialTheme.colorScheme.onSurface
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil de $firstName") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        isEditing = !isEditing
                    }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
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
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile_icon),
                    contentDescription = "Foto de Perfil",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )

                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Nome Completo") },
                    enabled = isEditing,
                    colors = customTextFieldColors,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = username,
                    onValueChange = {},
                    label = { Text("Nome de Usuário") },
                    enabled = false,
                    colors = customTextFieldColors,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    enabled = isEditing,
                    colors = customTextFieldColors,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Senha") },
                    enabled = isEditing,
                    visualTransformation = PasswordVisualTransformation(),
                    colors = customTextFieldColors,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = accessLevel,
                    onValueChange = {},
                    label = { Text("Nível de Acesso") },
                    enabled = false,
                    colors = customTextFieldColors,
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
