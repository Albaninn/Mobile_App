package br.edu.up.test_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import br.edu.up.test_app.ui.theme.Test_AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Test_AppTheme {
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Configuração da Navegação
                    NavigationComponent(navController)
                }
            }
        }
    }
}

// Mapa de usuários e senhas
val userCredentials = mapOf(
    "Lucas" to "1234",
    "Lorenna" to "qwe123",
    "Cesar" to "senha"
)

// Composable para navegação
@Composable
fun NavigationComponent(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login_screen") {
        // Tela de Login
        composable("login_screen") {
            LoginScreen(navController)
        }
        // Segunda tela (Card) com argumento para o nome do usuário
        composable(
            route = "second_screen/{username}",
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: "Desconhecido"
            SecondScreen(username)
        }
    }
}

// Tela de Login
@Composable
fun LoginScreen(navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf(false) }

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
                Text(text = "Usuário ou senha inválidos", color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Botão de login
            Button(
                onClick = {
                    // Verifica se o nome de usuário e senha correspondem
                    if (userCredentials.containsKey(username) && userCredentials[username] == password) {
                        loginError = false
                        // Navegar para a segunda tela passando o nome do usuário
                        navController.navigate("second_screen/$username")
                    } else {
                        loginError = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Entrar")
            }
        }
    }
}

// Segunda Tela (onde está o Card)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondScreen(username: String) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pagina Inicial") },
                navigationIcon = {
                    IconButton(onClick = { /* Handle menu click */ }) {
                        Icon(
                            imageVector = Icons.Default.Menu, // Ícone de menu padrão
                            contentDescription = "Menu"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Handle profile click */ }) {
                        // Aqui substituí o ic_profile pela imagem do usuário logado (profile_icon)
                        Image(
                            painter = painterResource(id = R.drawable.profile_icon),
                            contentDescription = "Perfil",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
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
            Card(Mensagem(username, "Olá, seja bem-vindo!"))
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

@Preview(showBackground = true)
@Composable
fun SecondScreenPreview() {
    Test_AppTheme {
        SecondScreen("Lucas")
    }
}
