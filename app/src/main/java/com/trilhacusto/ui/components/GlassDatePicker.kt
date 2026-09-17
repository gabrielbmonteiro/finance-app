package com.trilhacusto.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.trilhacusto.ui.theme.PrimaryAccent
import java.util.Calendar

@Composable
fun GlassDatePicker(
    initialDateMillis: Long,
    onDateSelected: (Long) -> Unit
) {
    var isSelectingMonthYear by remember { mutableStateOf(false) }

    val initialCal = Calendar.getInstance().apply { timeInMillis = initialDateMillis }
    var currentMonth by remember { mutableStateOf(initialCal.get(Calendar.MONTH)) }
    var currentYear by remember { mutableStateOf(initialCal.get(Calendar.YEAR)) }
    var selectedDateMillis by remember { mutableStateOf(initialDateMillis) }

    val meses = listOf("Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez")

    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        if (isSelectingMonthYear) {
            // MODO: Seleção de Mês e Ano (Igual ao Dashboard)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                IconButton(onClick = { currentYear-- }) {
                    Text("<", color = MaterialTheme.colorScheme.onSurface)
                }
                Text(
                    text = currentYear.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                IconButton(onClick = { currentYear++ }) {
                    Text(">", color = MaterialTheme.colorScheme.onSurface)
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(meses) { index, mes ->
                    val isSelected = index == currentMonth
                    val bgColor = if (isSelected) PrimaryAccent else Color.White.copy(alpha = 0.1f)
                    val textColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)

                    Box(
                        modifier = Modifier
                            .background(color = bgColor, shape = RoundedCornerShape(12.dp))
                            .clickable {
                                currentMonth = index
                                isSelectingMonthYear = false
                            }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = mes, color = textColor, fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            // MODO: Seleção de Dias
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                IconButton(onClick = {
                    if (currentMonth == 0) {
                        currentMonth = 11
                        currentYear--
                    } else {
                        currentMonth--
                    }
                }) {
                    Text("<", color = MaterialTheme.colorScheme.onSurface)
                }
                
                Text(
                    text = "${meses[currentMonth]} $currentYear",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.clickable { isSelectingMonthYear = true }.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                
                IconButton(onClick = {
                    if (currentMonth == 11) {
                        currentMonth = 0
                        currentYear++
                    } else {
                        currentMonth++
                    }
                }) {
                    Text(">", color = MaterialTheme.colorScheme.onSurface)
                }
            }

            // Dias da Semana
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                val diasSemana = listOf("D", "S", "T", "Q", "Q", "S", "S")
                diasSemana.forEach { dia ->
                    Text(
                        text = dia,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Grid de Dias
            val cal = Calendar.getInstance().apply {
                set(Calendar.YEAR, currentYear)
                set(Calendar.MONTH, currentMonth)
                set(Calendar.DAY_OF_MONTH, 1)
            }
            val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
            val firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1 // 0 for Sunday
            
            val totalCells = daysInMonth + firstDayOfWeek
            val rows = Math.ceil(totalCells / 7.0).toInt()

            Column(modifier = Modifier.fillMaxWidth()) {
                for (row in 0 until rows) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        for (col in 0 until 7) {
                            val cellIndex = row * 7 + col
                            val dayNumber = cellIndex - firstDayOfWeek + 1

                            if (cellIndex >= firstDayOfWeek && dayNumber <= daysInMonth) {
                                val dayCal = Calendar.getInstance().apply {
                                    set(Calendar.YEAR, currentYear)
                                    set(Calendar.MONTH, currentMonth)
                                    set(Calendar.DAY_OF_MONTH, dayNumber)
                                    set(Calendar.HOUR_OF_DAY, 12)
                                    set(Calendar.MINUTE, 0)
                                    set(Calendar.SECOND, 0)
                                    set(Calendar.MILLISECOND, 0)
                                }
                                
                                val selectedCal = Calendar.getInstance().apply { timeInMillis = selectedDateMillis }
                                val isSelected = selectedCal.get(Calendar.YEAR) == currentYear &&
                                                 selectedCal.get(Calendar.MONTH) == currentMonth &&
                                                 selectedCal.get(Calendar.DAY_OF_MONTH) == dayNumber

                                val bgColor = if (isSelected) PrimaryAccent else Color.Transparent
                                val textColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                        .padding(2.dp)
                                        .background(color = bgColor, shape = RoundedCornerShape(50))
                                        .clickable {
                                            selectedDateMillis = dayCal.timeInMillis
                                            onDateSelected(selectedDateMillis)
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = dayNumber.toString(), color = textColor, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                                }
                            } else {
                                Spacer(modifier = Modifier.weight(1f).aspectRatio(1f).padding(2.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
