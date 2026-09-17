package com.trilhacusto.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.trilhacusto.ui.theme.GlassBackground
import com.trilhacusto.ui.theme.GlassBorder

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    containerColor: androidx.compose.ui.graphics.Color = GlassBackground,
    content: @Composable BoxScope.() -> Unit
) {
    val roundedShape = RoundedCornerShape(24.dp)
    
    Box(
        modifier = modifier
            .clip(roundedShape)
    ) {
        // Camada de Fundo com o blur e a cor de vidro
        Box(
            modifier = Modifier
                .matchParentSize()
                .blur(radius = 16.dp)
                .background(color = containerColor)
                .border(width = 1.dp, color = GlassBorder, shape = roundedShape)
        )
        // Camada de Conteúdo sem o blur
        Box {
            content()
        }
    }
}
