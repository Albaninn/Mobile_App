package br.edu.up.test_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
            MyApp()
        }
    }
}

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
    "l.serenato" to UserProfile("Lucas Albano Ribas Serenato", "1234", "lk.serenato@example.com"),
    "lorenna.j" to UserProfile("Lorenna Judit", "qwe123", "lohve@example.com"),
    "cesar.a" to UserProfile("Cesar Augusto", "senha", "cesar@example.com")
)

// Data class para armazenar as informações do perfil
data class UserProfile(
    val fullName: String,
    val password: String,
    val email: String
)

// Composable para navegação
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
            SecondScreen(navController, username, isDarkTheme = isDarkTheme, onThemeChange)
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

@Composable
fun RegistrationScreen(navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
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
                                    userCredentials[username] = UserProfile(fullName, password, email)
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

// Segunda Tela (onde está o Card)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondScreen(navController: NavHostController, username: String, isDarkTheme: Boolean, onThemeChange: (Boolean) -> Unit) {
    // Buscar as informações do usuário a partir do mapa userCredentials
    val userProfile = userCredentials[username] ?: UserProfile("Desconhecido", "", "")

    // Extrair o primeiro nome do fullName
    val firstName = userProfile.fullName.split(" ").firstOrNull() ?: "Desconhecido"

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
                                                modifier = Modifier.size(32.dp, 16.dp) // Diminui o tamanho do Switch
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Tela2Screen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tela 2") },
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
            Text("Esta é a Tela 2", style = MaterialTheme.typography.headlineLarge)
        }
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
    val userProfile = userCredentials[username] ?: UserProfile("Desconhecido", "", "")

    // Extrair o primeiro nome do fullName
    val firstName = userProfile.fullName.split(" ").firstOrNull() ?: "Desconhecido"

    // Estado para controlar se o perfil está em modo de edição
    var isEditing by remember { mutableStateOf(false) }

    // Estado local para os campos de texto
    var fullName by remember { mutableStateOf(userProfile.fullName) }
    var email by remember { mutableStateOf(userProfile.email) }
    var password by remember { mutableStateOf(userProfile.password) }

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


