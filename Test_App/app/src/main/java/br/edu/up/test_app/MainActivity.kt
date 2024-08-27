package br.edu.up.test_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
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
import kotlinx.coroutines.launch

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

// Mapa de usuários com nome completo, senha e email
val userCredentials = mapOf(
    "Lucas" to UserProfile("Lucas Albano Ribas Serenato", "1234", "lk.serenato@example.com"),
    "Lorenna" to UserProfile("Lorenna Judit", "qwe123", "lohve@example.com"),
    "Cesar" to UserProfile("Cesar Augusto", "senha", "cesar@example.com")
)

// Data class para armazenar as informações do perfil
data class UserProfile(
    val fullName: String,
    val password: String,
    val email: String
)

// Composable para navegação
@Composable
fun NavigationComponent(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login_screen") {
        composable("login_screen") { LoginScreen(navController) }
        composable(
            route = "second_screen/{username}",
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: "Desconhecido"
            SecondScreen(navController, username)
        }
        composable("profile_screen/{username}",
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: "Desconhecido"
            ProfileScreen(navController, username)
        }
        composable("tela_1") { Tela1Screen(navController) }
        composable("tela_2") { Tela2Screen(navController) }
        composable("tela_3") { Tela3Screen(navController) }
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
                    if (userCredentials.containsKey(username) && userCredentials[username]?.password == password) {
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
fun SecondScreen(navController: NavHostController, username: String) {
    // Estado para controlar a abertura do drawer
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    // Itens do drawer
    val items = listOf("Tela 1", "Tela 2", "Tela 3")

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
                                "Tela 1" -> navController.navigate("tela_1")
                                "Tela 2" -> navController.navigate("tela_2")
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
                    title = { Text("Pagina Inicial") },
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

                                // Opção "Trocar Conta"
                                DropdownMenuItem(
                                    text = { Text("Trocar Conta") },
                                    onClick = {
                                        // Navegar para a tela de login novamente
                                        navController.navigate("login_screen")
                                        showMenu = false // Fechar o menu
                                    }
                                )

                                // Opção "Sair da Conta"
                                DropdownMenuItem(
                                    text = { Text("Sair da Conta") },
                                    onClick = {
                                        // Aqui você pode adicionar a lógica para sair da conta
                                        navController.navigate("login_screen")
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
                Card(Mensagem(username, "Olá, seja bem-vindo!"))
            }
        }
    }
}

@Composable
fun Tela1Screen(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Esta é a Tela 1", style = MaterialTheme.typography.headlineLarge)
    }
}

@Composable
fun Tela2Screen(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Esta é a Tela 2", style = MaterialTheme.typography.headlineLarge)
    }
}

@Composable
fun Tela3Screen(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Esta é a Tela 3", style = MaterialTheme.typography.headlineLarge)
    }
}



// Tela de Perfil do Usuário
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavHostController, username: String) {
    // Buscar as informações do usuário a partir do mapa userCredentials
    val userProfile = userCredentials[username] ?: UserProfile("Desconhecido", "", "")

    // Estado para controlar se o perfil está em modo de edição
    var isEditing by remember { mutableStateOf(false) }

    // Estado local para os campos de texto
    var fullName by remember { mutableStateOf(userProfile.fullName) }
    var email by remember { mutableStateOf(userProfile.email) }
    var password by remember { mutableStateOf(userProfile.password) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil de $username") },
                navigationIcon = {
                    IconButton(onClick = {
                        // Voltar para a segunda tela
                        navController.popBackStack() // Isso remove a tela atual da pilha de navegação e volta
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack, // Ícone de seta para voltar
                            contentDescription = "Voltar"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        // Alternar o modo de edição
                        isEditing = !isEditing
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

                // Cores customizadas para os campos desabilitados
                val customTextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface, // Cor do texto quando desabilitado
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant, // Cor do rótulo quando desabilitado
                    disabledBorderColor = MaterialTheme.colorScheme.onSurface // Cor da borda quando desabilitado
                )

                // Campo de nome completo
                OutlinedTextField(
                    value = fullName, // Preencher com o nome completo do perfil
                    onValueChange = { fullName = it },
                    label = { Text("Nome Completo") },
                    enabled = isEditing, // Habilita edição apenas se isEditing for true
                    colors = customTextFieldColors, // Define as cores customizadas
                    modifier = Modifier.fillMaxWidth()
                )

                // Campo de nome de usuário
                OutlinedTextField(
                    value = username, // Nome de usuário
                    onValueChange = {}, // Nome de usuário não deve ser editável
                    label = { Text("Nome de Usuário") },
                    enabled = false, // Sempre desabilitado
                    colors = customTextFieldColors, // Define as cores customizadas
                    modifier = Modifier.fillMaxWidth()
                )

                // Campo de email
                OutlinedTextField(
                    value = email, // Preencher com o email do perfil
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    enabled = isEditing, // Habilita edição apenas se isEditing for true
                    colors = customTextFieldColors, // Define as cores customizadas
                    modifier = Modifier.fillMaxWidth()
                )

                // Campo de senha
                OutlinedTextField(
                    value = password, // Preencher com a senha do perfil
                    onValueChange = { password = it },
                    label = { Text("Senha") },
                    enabled = isEditing, // Habilita edição apenas se isEditing for true
                    visualTransformation = PasswordVisualTransformation(),
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

@Preview(showBackground = true)
@Composable
fun SecondScreenPreview() {
    Test_AppTheme {
        SecondScreen(rememberNavController(), "Lucas")
    }
}
