package com.trilhacusto.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Animate from 0 to 1 over 2 seconds
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 2000,
                easing = FastOutSlowInEasing
            )
        )
        // Wait a little bit for the user to admire the logo
        delay(300)
        onSplashFinished()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(180.dp)) {
            val scale = size.width / 24f // scale factor to fit 24x24 path into 180dp canvas

            // Gradient brush (coordinates must match the unscaled 24x24 path space because of the canvas scale transform)
            val gradientBrush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFF00A3FF),
                    Color(0xFFB200FF)
                ),
                start = Offset(0f, 0f),
                end = Offset(24f, 24f)
            )

            // Stroke width should just be 1.5f (from SVG), because the canvas scale transform will scale it up automatically
            val stroke = Stroke(
                width = 1.5f,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )

            // Define the original paths
            val outerRectPath = Path().apply {
                moveTo(7f, 3f)
                lineTo(17f, 3f)
                arcTo(Rect(17f, 3f, 21f, 7f), -90f, 90f, false)
                lineTo(21f, 17f)
                arcTo(Rect(17f, 17f, 21f, 21f), 0f, 90f, false)
                lineTo(7f, 21f)
                arcTo(Rect(3f, 17f, 7f, 21f), 90f, 90f, false)
                lineTo(3f, 7f)
                arcTo(Rect(3f, 3f, 7f, 7f), 180f, 90f, false)
                close()
            }

            val vShapePath = Path().apply {
                moveTo(3f, 9f)
                lineTo(12f, 15f)
                lineTo(21f, 9f)
            }

            val verticalLinePath = Path().apply {
                moveTo(12f, 15f)
                lineTo(12f, 21f)
            }

            // Scale all paths to fit canvas
            val matrix = androidx.compose.ui.graphics.Matrix()
            matrix.scale(scale, scale)
            
            // To scale a path, we apply the matrix to a path or use drawContext scale, 
            // but we need to measure the path first and then scale, or scale first then measure.
            // Let's create a scaled version of the paths first.
            val scaledRectPath = Path().apply { addPath(outerRectPath) }
            // Path doesn't have an in-place transform in compose exactly like android.graphics.Path,
            // Actually, we can just use drawContext.transform.scale inside the drawing, and measure the original unscaled paths.
            
            val pmRect = PathMeasure()
            pmRect.setPath(outerRectPath, false)
            
            val pmV = PathMeasure()
            pmV.setPath(vShapePath, false)
            
            val pmLine = PathMeasure()
            pmLine.setPath(verticalLinePath, false)
            
            // Extract segments based on progress
            val segmentRect = Path()
            pmRect.getSegment(0f, pmRect.length * progress.value, segmentRect, true)
            
            val segmentV = Path()
            // We want V shape to start drawing slightly after rect starts
            val vProgress = (progress.value - 0.2f).coerceAtLeast(0f) / 0.8f
            pmV.getSegment(0f, pmV.length * vProgress, segmentV, true)
            
            val segmentLine = Path()
            // Vertical line starts drawing even later
            val lineProgress = (progress.value - 0.4f).coerceAtLeast(0f) / 0.6f
            pmLine.getSegment(0f, pmLine.length * lineProgress, segmentLine, true)

            // Scale the canvas then draw paths
            scale(scale, scale, Offset.Zero) {
                drawPath(
                    path = segmentRect,
                    brush = gradientBrush,
                    style = stroke
                )
                
                drawPath(
                    path = segmentV,
                    brush = gradientBrush,
                    style = stroke
                )
                
                drawPath(
                    path = segmentLine,
                    brush = gradientBrush,
                    style = stroke
                )
            }
        }
    }
}
