package com.fer.courses.biometrica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.biometric.*
import androidx.biometric.BiometricManager.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.core.content.ContextCompat
import com.fer.courses.biometrica.ui.theme.BiometricaTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BiometricaTheme {

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Auth()
                }
            }
        }
        setupAuth()
    }

    //Metodos privados para saber si el dispositivo tiene identificacion por hueella o facial
    private var canAuthenticate = false
    private lateinit var promtInfo: BiometricPrompt.PromptInfo
    private fun setupAuth(){
        if(BiometricManager.from(this).canAuthenticate(
                BiometricManager.Authenticators.BIOMETRIC_STRONG
                        or BiometricManager.Authenticators.DEVICE_CREDENTIAL) == androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS){
            canAuthenticate = true //Controlamos si podemos hacer la autenticacion
            promtInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Autenticación Biomética")
                .setSubtitle("Autentícate utilizando el sensor biométrico")
                //Metodos de autenticacion que vamos a permitir
                .setAllowedAuthenticators(androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
                        or androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                .build()
            //PromtInfo: Definir la pantalla de identificacion de usuario
        }
    }
    private fun authenticate(auth: (auth: Boolean) -> Unit){
        if(canAuthenticate){
            BiometricPrompt(this, ContextCompat.getMainExecutor(this),
                object: BiometricPrompt.AuthenticationCallback(){
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        auth(true)
                    }
                }).authenticate(promtInfo)
        }

    }
    @Composable
    fun Auth() {
        var auth by remember {
            mutableStateOf(false)
        }

        Column(modifier = Modifier
            .background(if(auth) White else Red)
            .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center)
        {
            Text(if(auth) "Estas autenticado!!!" else "Necesitas autenticarte", fontSize = 22.sp, fontWeight = FontWeight.Black )
            Spacer(Modifier.height(8.dp))
            Button(onClick = {
                /*TODO*/
                authenticate {
                    auth = it
                }

            }) {
                Text("Autenticar")
            }

        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        BiometricaTheme {
            Auth()
        }
    }

}
