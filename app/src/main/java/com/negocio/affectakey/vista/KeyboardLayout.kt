package com.negocio.affectakey.vista

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.negocio.affectakey.viewmodel.KeyboardViewModel

@Composable
fun KeyboardScreen(viewModel: KeyboardViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF212121))
            .padding(vertical = 4.dp, horizontal = 2.dp)
    ) {
        KeyboardRow(keys = viewModel.row1, onKeyClick = viewModel::onKeyPressed)
        KeyboardRow(keys = viewModel.row2, onKeyClick = viewModel::onKeyPressed)
        KeyboardRow(keys = viewModel.row3, onKeyClick = viewModel::onKeyPressed)
        KeyboardRow(keys = viewModel.row4, onKeyClick = viewModel::onKeyPressed)
    }
}

@Composable
fun KeyboardRow(
    keys: List<String>,
    onKeyClick: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        keys.forEach { key ->
            val weight = when (key) {
                " " -> 5f
                "⌫", "↵" -> 1.5f
                else -> 1f
            }

            KeyButton(
                text = key,
                modifier = Modifier.weight(weight),
                onClick = { onKeyClick(key) }
            )
        }
    }
}

@Composable
fun KeyButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .padding(2.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        color = Color(0xFF424242)
    ) {
        Box(
            modifier = Modifier.height(52.dp),
            contentAlignment = Alignment.Center
        ) {
            val displayText = when (text) {
                "⌫" -> "⌫"
                " " -> " "
                "↵" -> "↵"
                else -> text.uppercase()
            }
            Text(
                text = displayText,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}