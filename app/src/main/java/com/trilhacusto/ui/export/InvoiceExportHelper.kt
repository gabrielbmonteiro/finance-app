package com.trilhacusto.ui.export

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.trilhacusto.data.local.entity.PessoaEntity
import com.trilhacusto.data.local.relation.TransacaoCompleta
import com.trilhacusto.util.formatDateWithYearIfNeeded
import com.trilhacusto.util.toCurrencyString
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object InvoiceExportHelper {

    fun gerarArquivoExportacao(
        context: Context,
        transacoes: List<TransacaoCompleta>,
        todasPessoas: List<PessoaEntity>,
        formato: ExportFormat,
        isShare: Boolean,
        pessoasSelecionadas: Set<Long>
    ): Uri? {
        val fileName = "Fatura_TrilhaCusto_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}"
        
        val transacoesFiltradas = if (pessoasSelecionadas.isNotEmpty()) {
            transacoes.filter { tc ->
                tc.atribuicoes.filter { it.pessoa.id in pessoasSelecionadas }.sumOf { it.atribuicao.valorAtribuido } != 0.0
            }
        } else {
            transacoes
        }
        
        return if (formato == ExportFormat.PDF) {
            val pdfDoc = gerarPdf(transacoesFiltradas, todasPessoas, pessoasSelecionadas)
            salvarPdf(context, pdfDoc, fileName, isShare)
        } else {
            val bitmap = gerarBitmap(transacoesFiltradas, todasPessoas, pessoasSelecionadas)
            salvarBitmap(context, bitmap, fileName, isShare)
        }
    }

    private fun desenharConteudo(canvas: Canvas, transacoes: List<TransacaoCompleta>, todasPessoas: List<PessoaEntity>, pessoasSelecionadas: Set<Long>, width: Int) {
        val paintText = Paint().apply {
            color = Color.BLACK
            textSize = 14f
            isAntiAlias = true
        }
        val paintTitle = Paint(paintText).apply {
            textSize = 24f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        val paintHeader = Paint(paintText).apply {
            textSize = 16f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        val paintLine = Paint().apply {
            color = Color.LTGRAY
            strokeWidth = 1f
        }

        var y = 50f
        
        // Título
        canvas.drawText("Extrato de Fatura - TrilhaCusto", 50f, y, paintTitle)
        y += 30f
        canvas.drawText("Gerado em: ${SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())}", 50f, y, paintText)
        y += 50f
        
        // Cabeçalho Tabela
        canvas.drawText("Data", 50f, y, paintHeader)
        canvas.drawText("Descrição", 150f, y, paintHeader)
        canvas.drawText("Responsáveis", 400f, y, paintHeader)
        canvas.drawText("Valor", width - 150f, y, paintHeader)
        y += 15f
        canvas.drawLine(50f, y, width - 50f, y, paintLine)
        y += 25f

        // Itens
        for (transacaoCompleta in transacoes) {
            val t = transacaoCompleta.transacao
            val desc = t.descricaoCustomizada ?: t.descricaoOriginal
            val dataStr = t.dataHora.formatDateWithYearIfNeeded(includeTime = false)
            val valorFatura = if (pessoasSelecionadas.isNotEmpty()) {
                transacaoCompleta.atribuicoes.filter { it.pessoa.id in pessoasSelecionadas }.sumOf { it.atribuicao.valorAtribuido }
            } else {
                t.valorTotal
            }
            val valorStr = valorFatura.toCurrencyString(false)
            
            val respStr = if (pessoasSelecionadas.isNotEmpty()) {
                transacaoCompleta.atribuicoes.filter { it.pessoa.id in pessoasSelecionadas }.joinToString(", ") { it.pessoa.nome }
            } else {
                transacaoCompleta.atribuicoes.joinToString(", ") { it.pessoa.nome }
            }
            
            canvas.drawText(dataStr, 50f, y, paintText)
            
            // Truncate description if too long
            var displayDesc = desc
            if (paintText.measureText(displayDesc) > 230f) {
                while (paintText.measureText(displayDesc + "...") > 230f && displayDesc.isNotEmpty()) {
                    displayDesc = displayDesc.substring(0, displayDesc.length - 1)
                }
                displayDesc += "..."
            }
            canvas.drawText(displayDesc, 150f, y, paintText)
            
            // Truncate responsaveis
            var displayResp = respStr
            if (paintText.measureText(displayResp) > 180f) {
                while (paintText.measureText(displayResp + "...") > 180f && displayResp.isNotEmpty()) {
                    displayResp = displayResp.substring(0, displayResp.length - 1)
                }
                displayResp += "..."
            }
            canvas.drawText(displayResp, 400f, y, paintText)
            
            canvas.drawText(valorStr, width - 150f, y, paintText)
            
            y += 25f
            canvas.drawLine(50f, y, width - 50f, y, paintLine)
            y += 25f
        }
        
        val total = if (pessoasSelecionadas.isNotEmpty()) {
            transacoes.flatMap { it.atribuicoes }.filter { it.pessoa.id in pessoasSelecionadas }.sumOf { it.atribuicao.valorAtribuido }
        } else {
            transacoes.sumOf { it.transacao.valorTotal }
        }
        y += 20f
        canvas.drawText("Total: ${total.toCurrencyString(false)}", width - 250f, y, paintHeader)
    }

    private fun gerarPdf(transacoes: List<TransacaoCompleta>, todasPessoas: List<PessoaEntity>, pessoasSelecionadas: Set<Long>): PdfDocument {
        val pdfDocument = PdfDocument()
        val estimatedHeight = 200 + (transacoes.size * 50) + 100
        val pageHeight = maxOf(1122, estimatedHeight)
        val pageInfo = PdfDocument.PageInfo.Builder(792, pageHeight, 1).create() // A4 width, dynamic height
        val page = pdfDocument.startPage(pageInfo)
        
        desenharConteudo(page.canvas, transacoes, todasPessoas, pessoasSelecionadas, 792)
        
        pdfDocument.finishPage(page)
        return pdfDocument
    }

    private fun gerarBitmap(transacoes: List<TransacaoCompleta>, todasPessoas: List<PessoaEntity>, pessoasSelecionadas: Set<Long>): Bitmap {
        val width = 800
        val height = 200 + (transacoes.size * 50) + 100 // Estimate height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
        
        desenharConteudo(canvas, transacoes, todasPessoas, pessoasSelecionadas, width)
        
        return bitmap
    }

    private fun salvarPdf(context: Context, pdfDocument: PdfDocument, fileName: String, isShare: Boolean): Uri? {
        val name = "$fileName.pdf"
        try {
            if (isShare) {
                val file = File(context.cacheDir, name)
                FileOutputStream(file).use { pdfDocument.writeTo(it) }
                pdfDocument.close()
                return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            } else {
                return saveToDownloads(context, name, "application/pdf") { os ->
                    pdfDocument.writeTo(os)
                    pdfDocument.close()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun salvarBitmap(context: Context, bitmap: Bitmap, fileName: String, isShare: Boolean): Uri? {
        val name = "$fileName.jpg"
        try {
            if (isShare) {
                val file = File(context.cacheDir, name)
                FileOutputStream(file).use { bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it) }
                return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            } else {
                return saveToDownloads(context, name, "image/jpeg") { os ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, os)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun saveToDownloads(context: Context, displayName: String, mimeType: String, writeAction: (OutputStream) -> Unit): Uri? {
        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/TrilhaCusto")
            }
        }

        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Downloads.EXTERNAL_CONTENT_URI
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI // For older versions as fallback, though might not work perfectly for PDFs
        }

        val uri = resolver.insert(collection, contentValues)
        if (uri != null) {
            resolver.openOutputStream(uri)?.use { os ->
                writeAction(os)
            }
        }
        return uri
    }
}
