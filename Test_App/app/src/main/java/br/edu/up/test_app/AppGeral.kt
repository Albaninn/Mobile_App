package br.edu.up.test_app

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
import br.edu.up.test_app.screen.AdmScreen
import br.edu.up.test_app.screen.usoGeral.BancoHorasScreen
import br.edu.up.test_app.screen.BaseScreen
import br.edu.up.test_app.screen.usoGeral.CheckListScreen
import br.edu.up.test_app.screen.inicio_perfil.LoginScreen
import br.edu.up.test_app.screen.inicio_perfil.ProfileScreen
import br.edu.up.test_app.screen.inicio_perfil.RegistrationScreen
import br.edu.up.test_app.screen.usoGeral.checkList.UtilizacaoVeiculosScreen
import br.edu.up.test_app.ui.theme.Test_AppTheme

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
            BaseScreen(navController, username, isDarkTheme = isDarkTheme, onThemeChange)
        }
        composable("profile_screen/{username}",
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: "Desconhecido"
            ProfileScreen(navController, username)
        }
        composable("tela_1") { AdmScreen(navController) }
        composable(
            route = "tela_2/{username}",
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: "Desconhecido"
            BancoHorasScreen(navController, username)
        }
        composable("tela_3") { CheckListScreen(navController) }
        composable("utilizacao_de_veiculos") { UtilizacaoVeiculosScreen(navController) }
        composable("tela_4") { ExScreen(navController) }
        composable("ex_1") { Ex1(navController) }
        composable("ex_2") { Ex2(navController) }
        composable("ex_3") { Ex3(navController) }
        composable("ex_4") { Ex4(navController) }
    }
}

@Preview
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppPerkons() {
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