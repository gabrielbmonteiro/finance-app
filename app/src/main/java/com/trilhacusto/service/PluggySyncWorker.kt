package com.trilhacusto.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.trilhacusto.MainActivity
import com.trilhacusto.R
import com.trilhacusto.data.local.dao.ConfiguracoesDao
import com.trilhacusto.data.local.dao.TransacaoDao
import com.trilhacusto.data.remote.repository.PluggyNetworkRepository
import com.trilhacusto.domain.usecase.ProcessarTransacaoPluggyUseCase
import com.trilhacusto.domain.usecase.TransacaoPluggyDto
import kotlinx.coroutines.flow.firstOrNull
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PluggySyncWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

    private val pluggyNetworkRepository: PluggyNetworkRepository by inject()
    private val processarTransacaoPluggyUseCase: ProcessarTransacaoPluggyUseCase by inject()
    private val configuracoesDao: ConfiguracoesDao by inject()
    private val transacaoDao: TransacaoDao by inject()

    override suspend fun doWork(): Result {
        Log.d("PluggySyncWorker", "Iniciando sincronização em background...")
        return try {
            val accountId = com.trilhacusto.BuildConfig.PLUGGY_ACCOUNT_ID
            
            // 1. Contar pendências atuais
            val pendentesAntes = transacaoDao.getTransacoesPendentesList().count { it.transacao.statusAtribuicao == "PENDENTE" }
            
            // 2. Fazer requisição
            val result = pluggyNetworkRepository.fetchTransactions(accountId)
            
            if (result.isSuccess) {
                val transactions = result.getOrNull() ?: emptyList()
                
                // Pegar limites da fatura
                val config = configuracoesDao.getConfiguracoes().firstOrNull()
                val fechamento = config?.diaFechamento ?: 25
                val c = Calendar.getInstance()
                
                val inicioFaturaAtual = Calendar.getInstance().apply {
                    timeInMillis = c.timeInMillis
                    add(Calendar.MONTH, -1)
                    set(Calendar.DAY_OF_MONTH, fechamento + 1)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                }.timeInMillis

                val dtos = transactions.map {
                    TransacaoPluggyDto(
                        id = it.id,
                        amount = it.amount,
                        description = it.merchant?.name ?: it.merchant?.businessName ?: it.description,
                        date = try {
                            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
                            format.parse(it.date)?.time ?: System.currentTimeMillis()
                        } catch (e: Exception) {
                            System.currentTimeMillis()
                        },
                        numeroParcela = it.creditCardMetadata?.installmentNumber,
                        totalParcelas = it.creditCardMetadata?.totalInstallments
                    )
                }.filter { it.date >= inicioFaturaAtual }

                // 3. Processar
                processarTransacaoPluggyUseCase(dtos)
                
                // 4. Checar pendências de novo
                val pendentesDepois = transacaoDao.getTransacoesPendentesList().count { it.transacao.statusAtribuicao == "PENDENTE" }
                val novasPendencias = pendentesDepois - pendentesAntes
                
                if (novasPendencias > 0) {
                    enviarNotificacao(novasPendencias)
                }
                
                Log.d("PluggySyncWorker", "Sincronização concluída. Novas pendências: $novasPendencias")
                Result.success()
            } else {
                Log.e("PluggySyncWorker", "Erro na API da Pluggy: ${result.exceptionOrNull()?.message}")
                Result.retry()
            }
        } catch (e: Exception) {
            Log.e("PluggySyncWorker", "Erro inesperado na sincronização", e)
            Result.retry()
        }
    }

    private fun enviarNotificacao(qtdPendencias: Int) {
        val channelId = "TrilhaCusto_sync_channel"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Intent para abrir o app na aba de pendências (pode ser o Dashboard/Pendências)
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setColor(ContextCompat.getColor(context, android.R.color.holo_orange_dark))
            .setContentTitle("Novas Despesas no TrilhaCusto")
            .setContentText("Você tem $qtdPendencias nova(s) pendência(s) para classificar.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }
}
