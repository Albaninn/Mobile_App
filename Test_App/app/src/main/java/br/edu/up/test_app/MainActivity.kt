package br.edu.up.test_app

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import br.edu.up.test_app.screen.BancoHorasScreen
import br.edu.up.test_app.screen.LoginScreen
import br.edu.up.test_app.screen.ProfileScreen
import br.edu.up.test_app.screen.RegistrationScreen
import br.edu.up.test_app.screen.Tela1Screen
import br.edu.up.test_app.screen.Tela3Screen
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

@Preview
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
            BancoHorasScreen(navController, username)
        }
        composable("tela_3") { Tela3Screen(navController) }
        composable("tela_4") { ExScreen(navController) }
        composable("ex_1") { Ex1(navController) }
        composable("ex_2") { Ex2(navController) }
        composable("ex_3") { Ex3(navController) }
        composable("ex_4") { Ex4(navController) }
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