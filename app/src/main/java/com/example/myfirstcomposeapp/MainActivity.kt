package com.example.myfirstcomposeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myfirstcomposeapp.data.db.AppDatabase
import com.example.myfirstcomposeapp.data.repository.ChangeRepository
import com.example.myfirstcomposeapp.ui.navigation.AppNavHost
import com.example.myfirstcomposeapp.ui.theme.MyFirstComposeAppTheme
import com.example.myfirstcomposeapp.ui.viewmodel.ChangeViewModel
import com.example.myfirstcomposeapp.ui.viewmodel.ChangeViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Room -> Repo -> Factory
        val db = AppDatabase.get(this)
        val repo = ChangeRepository(db.changeDao(), db.changeLogDao())
        val factory = ChangeViewModelFactory(repo)

        setContent {
            MyFirstComposeAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val vm: ChangeViewModel = viewModel(factory = factory)
                    // 注意：AppNavHost 只接收 vm，自行创建 navController
                    AppNavHost(vm = vm)
                }
            }
        }
    }
}