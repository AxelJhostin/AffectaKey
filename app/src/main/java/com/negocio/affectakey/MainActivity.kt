package com.negocio.affectakey

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.negocio.affectakey.ui.theme.AffectaKeyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AffectaKeyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    InstructionScreen()
                }
            }
        }
    }
}

@Composable
fun InstructionScreen() {
    // Necesitamos el 'Context' para lanzar la pantalla de Ajustes
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // Para que quepa en pantallas pequeñas
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "¡Bienvenido a AffectaKey!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Tu teclado para el bienestar emocional.",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- Explicación de cómo funciona ---
        Text(
            text = "¿Cómo funciona?",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "AffectaKey analiza pasivamente tus patrones de tecleo (velocidad, errores, pausas) y el uso de emojis para inferir tu estado emocional, especialmente el estrés.\n\nTodo el análisis se realiza de forma 100% privada en tu dispositivo.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // --- Pasos de activación ---
        Text(
            text = "Cómo Activarlo",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        InstructionStep(
            number = "1",
            text = "Presiona el botón de abajo ('Ir a Ajustes')."
        )
        InstructionStep(
            number = "2",
            text = "Busca 'AffectaKey' en la lista y activa el interruptor."
        )
        InstructionStep(
            number = "3",
            text = "Abre cualquier app (ej. un chat) y toca para escribir."
        )
        InstructionStep(
            number = "4",
            text = "Toca el ícono ⌨️ (en la esquina o en la barra de notificaciones) y selecciona 'AffectaKey'."
        )

        Spacer(modifier = Modifier.height(32.dp))

        // --- Botón de Call to Action ---
        Button(
            onClick = {
                // Esta es la 'Intención' que abre la configuración de teclados
                val intent = Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            Text("Ir a Ajustes de Teclado", fontSize = 16.sp)
        }
    }
}

@Composable
fun InstructionStep(number: String, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "$number.",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(end = 12.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InstructionScreenPreview() {
    AffectaKeyTheme {
        InstructionScreen()
    }
}