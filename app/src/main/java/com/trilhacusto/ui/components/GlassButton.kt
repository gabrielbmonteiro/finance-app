package com.trilhacusto.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun GlassButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val electricBlue = Color(0xFF2563EB)
    val softPurple = Color(0xFF8B5CF6)
    val glassDisabledColor = com.trilhacusto.ui.theme.GlassBackground
    
    val shape = RoundedCornerShape(percent = 50)
    
    val gradientBrush = Brush.linearGradient(
        colors = listOf(electricBlue, softPurple)
    )

    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth(),
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent
        ),
        contentPadding = PaddingValues(0.dp) // We will handle padding inside the Box
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = if (enabled) gradientBrush else Brush.linearGradient(listOf(glassDisabledColor, glassDisabledColor)),
                    shape = shape
                )
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = if (enabled) com.trilhacusto.ui.theme.TextPrimary else com.trilhacusto.ui.theme.TextSecondary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
