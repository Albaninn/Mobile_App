package br.edu.up.test_app

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.edu.up.test_app.ex.Ex1
import br.edu.up.test_app.ex.Ex2
import br.edu.up.test_app.ex.Ex3
import br.edu.up.test_app.ex.Ex4
import br.edu.up.test_app.ex.ExScreen
import br.edu.up.test_app.ui.theme.Test_AppTheme
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month
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
        WorkDay("04/03/2024", "08:00", "18:00"),
        WorkDay("05/03/2024", "08:00", "17:56"),
        WorkDay("06/03/2024", "07:31", "18:02"),
        WorkDay("07/03/2024", "08:19", "17:44"),
        WorkDay("08/03/2024", "07:28", "18:01"),
        WorkDay("11/03/2024", "08:21", "17:35"),
        WorkDay("12/03/2024", "08:27", "17:49"),
        WorkDay("13/03/2024", "07:21", "17:59"),
        WorkDay("14/03/2024", "07:25", "17:52"),
        WorkDay("15/03/2024", "07:29", "17:54"),
        WorkDay("18/03/2024", "08:31", "17:55"),
        WorkDay("19/03/2024", "08:21", "17:34"),
        WorkDay("21/03/2024", "07:28", "18:14"),
        WorkDay("22/03/2024", "07:44", "16:54"),
        WorkDay("25/03/2024", "08:19", "17:41"),
        WorkDay("26/03/2024", "08:25", "17:50"),
        WorkDay("27/03/2024", "07:30", "17:49"),
        WorkDay("28/03/2024", "07:28", "18:20"),
        WorkDay("01/04/2024", "08:21", "17:52"),
        WorkDay("02/04/2024", "08:21", "17:51"),
        WorkDay("03/04/2024", "07:39", "17:57"),
        WorkDay("04/04/2024", "07:51", "18:07"),
        WorkDay("05/04/2024", "08:02", "17:59"),
        WorkDay("08/04/2024", "08:18", "17:51"),
        WorkDay("09/04/2024", "08:25", "17:53"),
        WorkDay("10/04/2024", "07:37", "17:58"),
        WorkDay("11/04/2024", "07:40", "18:02"),
        WorkDay("12/04/2024", "08:29", "17:57"),
        WorkDay("15/04/2024", "08:28", "17:48"),
        WorkDay("16/04/2024", "08:17", "17:51"),
        WorkDay("17/04/2024", "07:30", "18:07"),
        WorkDay("18/04/2024", "07:25", "17:37"),
        WorkDay("19/04/2024", "08:19", "17:53"),
        WorkDay("22/04/2024", "08:19", "17:55"),
        WorkDay("23/04/2024", "08:21", "17:50"),
        WorkDay("24/04/2024", "07:30", "18:02"),
        WorkDay("25/04/2024", "07:26", "18:00"),
        WorkDay("26/04/2024", "07:22", "17:55"),
        WorkDay("29/04/2024", "08:18", "17:51"),
        WorkDay("30/04/2024", "07:27", "18:08"),
        WorkDay("02/05/2024", "07:25", "18:02"),
        WorkDay("03/05/2024", "07:48", "18:03"),
        WorkDay("06/05/2024", "08:17", "17:53"),
        WorkDay("07/05/2024", "08:16", "17:50"),
        WorkDay("08/05/2024", "07:31", "18:02"),
        WorkDay("09/05/2024", "07:30", "18:00"),
        WorkDay("10/05/2024", "08:00", "17:07"),
        WorkDay("13/05/2024", "08:17", "17:44"),
        WorkDay("14/05/2024", "08:22", "17:47"),
        WorkDay("15/05/2024", "07:33", "18:00"),
        WorkDay("16/05/2024", "07:31", "18:11"),
        WorkDay("20/05/2024", "07:45", "17:29"),
        WorkDay("21/05/2024", "08:27", "17:46"),
        WorkDay("22/05/2024", "07:54", "18:14"),
        WorkDay("23/05/2024", "07:31", "18:10"),
        WorkDay("27/05/2024", "08:41", "17:46"),
        WorkDay("28/05/2024", "08:35", "17:54"),
        WorkDay("29/05/2024", "07:26", "17:40"),
        WorkDay("31/05/2024", "07:43", "18:07"),
        WorkDay("03/06/2024", "07:27", "17:55"),
        WorkDay("04/06/2024", "07:20", "17:32"),
        WorkDay("05/06/2024", "07:30", "18:06"),
        WorkDay("06/06/2024", "07:23", "17:51"),
        WorkDay("07/06/2024", "08:33", "17:38"),
        WorkDay("10/06/2024", "07:35", "18:05"),
        WorkDay("11/06/2024", "07:29", "18:08"),
        WorkDay("12/06/2024", "07:28", "17:50"),
        WorkDay("13/06/2024", "07:25", "17:45"),
        WorkDay("14/06/2024", "08:05", "18:10"),
        WorkDay("17/06/2024", "07:56", "17:47"),
        WorkDay("18/06/2024", "07:31", "18:25"),
        WorkDay("19/06/2024", "07:49", "18:00"),
        WorkDay("20/06/2024", "07:46", "18:16"),
        WorkDay("21/06/2024", "07:52", "18:05"),
        WorkDay("24/06/2024", "08:24", "17:49"),
        WorkDay("25/06/2024", "08:25", "17:57"),
        WorkDay("26/06/2024", "07:45", "17:50"),
        WorkDay("27/06/2024", "07:14", "17:20"),
        WorkDay("28/06/2024", "08:03", "18:07"),
        WorkDay("01/07/2024", "07:56", "17:49"),
        WorkDay("02/07/2024", "08:04", "17:49"),
        WorkDay("03/07/2024", "08:01", "17:44"),
        WorkDay("04/07/2024", "07:54", "18:10"),
        WorkDay("05/07/2024", "07:57", "17:56"),
        WorkDay("08/07/2024", "08:08", "17:49"),
        WorkDay("09/07/2024", "07:41", "17:42"),
        WorkDay("10/07/2024", "07:26", "18:09"),
        WorkDay("11/07/2024", "07:45", "18:23"),
        WorkDay("12/07/2024", "08:23", "17:23"),
        WorkDay("15/07/2024", "07:57", "17:33"),
        WorkDay("16/07/2024", "07:25", "18:04"),
        WorkDay("17/07/2024", "07:30", "18:08"),
        WorkDay("18/07/2024", "08:26", "18:00"),
        WorkDay("19/07/2024", "08:20", "17:55"),
        WorkDay("22/07/2024", "07:57", "17:46"),
        WorkDay("23/07/2024", "08:32", "17:54"),
        WorkDay("24/07/2024", "07:25", "18:18"),
        WorkDay("25/07/2024", "07:33", "17:50"),
        WorkDay("26/07/2024", "07:53", "17:36"),
        WorkDay("29/07/2024", "08:10", "17:35"),
        WorkDay("30/07/2024", "08:07", "17:47"),
        WorkDay("31/07/2024", "07:40", "18:19"),
        WorkDay("01/08/2024", "07:25", "18:15"),
        WorkDay("02/08/2024", "07:57", "17:33"),
        WorkDay("05/08/2024", "08:38", "17:45"),
        WorkDay("06/08/2024", "08:12", "17:53"),
        WorkDay("07/08/2024", "07:20", "18:05"),
        WorkDay("08/08/2024", "07:30", "18:15"),
        WorkDay("09/08/2024", "07:29", "15:49"),
        WorkDay("12/08/2024", "08:15", "17:47"),
        WorkDay("13/08/2024", "08:17", "17:38"),
        WorkDay("14/08/2024", "07:18", "18:10"),
        WorkDay("15/08/2024", "07:10", "18:10"),
        WorkDay("16/08/2024", "07:48", "18:02"),
        WorkDay("19/08/2024", "08:15", "17:43"),
        WorkDay("20/08/2024", "08:18", "17:41"),
        WorkDay("21/08/2024", "07:04", "18:03"),
        WorkDay("22/08/2024", "07:13", "18:03"),
        WorkDay("23/08/2024", "07:55", "17:46"),
        WorkDay("26/08/2024", "08:04", "17:47"),
        WorkDay("27/08/2024", "09:53", "17:28"),
        WorkDay("28/08/2024", "08:32", "18:16"),
        WorkDay("29/08/2024", "07:13", "18:05"),
        WorkDay("30/08/2024", "08:56", "16:23"),
        WorkDay("02/09/2024", "08:35", "17:38"),
        WorkDay("03/09/2024", "09:27", "17:33"),
        WorkDay("04/09/2024", "07:24", "17:45"),
        WorkDay("05/09/2024", "07:55", "18:06"),
        WorkDay("06/09/2024", "09:33", "16:06"),
        WorkDay("09/09/2024", "08:38", "12:00")

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
        composable("tela_4") { ExScreen(navController) }
        composable("ex_1") { Ex1(navController) }
        composable("ex_2") { Ex2(navController) }
        composable("ex_3") { Ex3(navController) }
        composable("ex_4") { Ex4(navController) }
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
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())  // Permite rolagem
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
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
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())  // Permite rolagem
                .padding(16.dp)
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

    val items = mutableListOf("Consulta de Hora", "Tela 3", "Exercicios")

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
                                "Exercicios" -> navController.navigate("tela_4")
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
                    .verticalScroll(rememberScrollState())  // Permite rolagem
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
                title = { Text("Administrativo") },
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
            Text("Esta é a Tela 1", style = MaterialTheme.typography.headlineLarge)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Tela2Screen(navController: NavHostController, username: String) {
    var isEditing by remember { mutableStateOf(false) }
    var showInReais by remember { mutableStateOf(false) }
    var selectedMonth by remember { mutableStateOf<Month?>(null) }

    // Estados para horas positivas e negativas
    var totalPositiveHours by remember { mutableStateOf(calculateTotalPositiveHours(username)) }
    var totalNegativeHours by remember { mutableStateOf(calculateTotalNegativeHours(username)) }
    val valorHora = 16.65

    // Calcular a diferença entre horas positivas e negativas
    val saldoHoras = totalPositiveHours - totalNegativeHours

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
                    IconButton(onClick = {
                        isEditing = !isEditing
                        if (!isEditing) {
                            // Recalcular os totais quando a edição é concluída
                            totalPositiveHours = calculateTotalPositiveHours(username)
                            totalNegativeHours = calculateTotalNegativeHours(username)
                        }
                    }) {
                        Icon(
                            imageVector = if (isEditing) Icons.Default.Check else Icons.Default.Edit,
                            contentDescription = if (isEditing) "Salvar" else "Editar"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())  // Habilita rolagem
                .padding(padding)
        ) {
            // Saldo do Banco de Horas
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = if (saldoHoras >= 0) R.drawable.ampfull else R.drawable.amp),
                        contentDescription = if (saldoHoras >= 0) "Saldo Positivo" else "Saldo Negativo",
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = if (showInReais) {
                            "R\$ ${String.format("%.2f", saldoHoras * valorHora)}"
                        } else {
                            formatHoursToHHmm(saldoHoras)
                        },
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = if (saldoHoras >= 0) "Saldo Positivo" else "Saldo Negativo",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                // Toggle entre Horas e Reais
                IconButton(onClick = { showInReais = !showInReais }) {
                    Image(
                        painter = painterResource(id = R.drawable.money),
                        contentDescription = if (showInReais) "Mostrar em Horas" else "Mostrar em Reais",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Filtro por tempo
            FilterByTime(selectedMonth) { month ->
                selectedMonth = month
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Exibir a lista de registros de banco de horas do usuário logado
            WorkHoursList(username = username, isEditing = isEditing, selectedMonth = selectedMonth)
        }
    }
}


// Função para formatar as horas em "HH:mm"
@RequiresApi(Build.VERSION_CODES.O)
fun formatHoursToHHmm(hours: Double): String {
    val totalMinutes = (hours * 60).toInt()
    val hh = totalMinutes / 60
    val mm = totalMinutes % 60
    return if (hours >= 0) {
        "%02d:%02d".format(hh, mm)
    } else {
        "- %02d:%02d".format(-hh, -mm)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FilterByTime(selectedMonth: Month?, onMonthSelected: (Month?) -> Unit) {
    val months = Month.values()
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(text = selectedMonth?.name ?: "Selecionar Mês")
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Todos") },
                onClick = {
                    onMonthSelected(null)
                    expanded = false
                }
            )
            months.forEach { month ->
                DropdownMenuItem(
                    text = { Text(month.name) },
                    onClick = {
                        onMonthSelected(month)
                        expanded = false
                    }
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun calculateTotalPositiveHours(username: String): Double {
    val userWorkDays = workHours[username] ?: emptyList()
    return userWorkDays.sumOf { workDay ->
        val totalHours = parseTime(workDay.totalAdjustedHours)
        val standardHours = if (workDay.dayOfWeek.equals("sexta-feira", ignoreCase = true)) {
            LocalTime.of(8, 0)
        } else {
            LocalTime.of(9, 0)
        }
        val positiveMinutes = ChronoUnit.MINUTES.between(standardHours, totalHours).takeIf { it > 0 } ?: 0
        positiveMinutes / 60.0 + (positiveMinutes % 60) / 100.0
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun calculateTotalNegativeHours(username: String): Double {
    val userWorkDays = workHours[username] ?: emptyList()
    return userWorkDays.sumOf { workDay ->
        val totalHours = parseTime(workDay.totalAdjustedHours)
        val standardHours = if (workDay.dayOfWeek.equals("sexta-feira", ignoreCase = true)) {
            LocalTime.of(8, 0)
        } else {
            LocalTime.of(9, 0)
        }
        val negativeMinutes = ChronoUnit.MINUTES.between(totalHours, standardHours).takeIf { it > 0 } ?: 0
        negativeMinutes / 60.0 + (negativeMinutes % 60) / 100.0
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WorkHoursList(username: String, isEditing: Boolean, selectedMonth: Month?) {
    val workDays = workHours[username]?.sortedByDescending {
        LocalDate.parse(it.date, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    }?.filter {
        selectedMonth == null || LocalDate.parse(it.date, DateTimeFormatter.ofPattern("dd/MM/yyyy")).month == selectedMonth
    } ?: emptyList()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Cabeçalho da tabela
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Data", modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
            Text(text = "Entrada", modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
            Text(text = "Saída", modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
            Text(text = "Total", modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
        }

        Divider()

        // Itens da tabela
        workDays.forEach { workDay ->
            WorkDayItem(workDay, isEditing)
            Divider()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WorkDayItem(workDay: WorkDay, isEditing: Boolean) {
    var entryTime by remember { mutableStateOf(workDay.entryTime) }
    var exitTime by remember { mutableStateOf(workDay.exitTime) }

    // Função para validar e formatar as horas
    fun parseAndFormatTime(time: String): String {
        return try {
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            val parsedTime = LocalTime.parse(time, formatter)
            parsedTime.format(formatter)
        } catch (e: Exception) {
            "Invalid"
        }
    }

    // Recalcula total de horas, crédito e débito toda vez que as horas são editadas
    val totalHours by remember(entryTime, exitTime) {
        mutableStateOf(
            if (parseAndFormatTime(entryTime) != "Invalid" && parseAndFormatTime(exitTime) != "Invalid") {
                calculateTotalAdjustedHours(entryTime, exitTime, workDay.dayOfWeek)
            } else {
                "Invalid"
            }
        )
    }
    val credit by remember(totalHours) {
        mutableStateOf(
            if (totalHours != "Invalid") calculateCredit(totalHours, workDay.dayOfWeek) else "00:00"
        )
    }
    val debit by remember(totalHours) {
        mutableStateOf(
            if (totalHours != "Invalid") calculateDebit(totalHours, workDay.dayOfWeek) else "00:00"
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Coluna de Data e Dia da Semana
        Column(modifier = Modifier.weight(1f)) {
            Text(text = workDay.date, style = MaterialTheme.typography.bodyMedium)
            Text(text = workDay.dayOfWeek, style = MaterialTheme.typography.bodySmall)
        }

        // Coluna de Entrada
        if (isEditing) {
            OutlinedTextField(
                value = entryTime,
                onValueChange = { newTime ->
                    entryTime = newTime
                },
                label = { Text("Entrada") },
                singleLine = true,
                isError = parseAndFormatTime(entryTime) == "Invalid",
                modifier = Modifier.weight(1f)
            )
        } else {
            Text(text = entryTime, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
        }

        // Coluna de Saída
        if (isEditing) {
            OutlinedTextField(
                value = exitTime,
                onValueChange = { newTime ->
                    exitTime = newTime
                },
                label = { Text("Saída") },
                singleLine = true,
                isError = parseAndFormatTime(exitTime) == "Invalid",
                modifier = Modifier.weight(1f)
            )
        } else {
            Text(text = exitTime, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
        }

        // Coluna de Total de Horas e Crédito/Débito
        Column(modifier = Modifier.weight(1f)) {
            if (totalHours == "Invalid") {
                Text(text = "Erro", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.error)
            } else {
                Text(text = totalHours, style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = if (credit != "00:00") "$credit Crédito" else "$debit Débito",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (credit != "00:00") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
            }
        }
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
                .verticalScroll(rememberScrollState())  // Permite rolagem
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
                            imageVector = if (isEditing) Icons.Default.CheckCircle else Icons.Default.Edit,
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
                .verticalScroll(rememberScrollState())  // Permite rolagem
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
