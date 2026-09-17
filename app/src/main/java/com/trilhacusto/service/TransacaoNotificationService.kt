package com.trilhacusto.service

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.trilhacusto.domain.usecase.ProcessarTransacaoPluggyUseCase
import com.trilhacusto.domain.usecase.TransacaoPluggyDto
import com.trilhacusto.util.NotificationParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.UUID

class TransacaoNotificationService : NotificationListenerService() {


    private val job = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.IO + job)




    lateinit var processarUseCase: ProcessarTransacaoPluggyUseCase

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        sbn ?: return

        val packageName = sbn.packageName
        Log.d("TrilhaCusto", "Notificação interceptada do pacote: $packageName")



        if (packageName != "com.xpi.investimentos" && packageName != "com.nu.production") {
            return
        }

        val extras = sbn.notification.extras
        val title = extras.getString(Notification.EXTRA_TITLE) ?: ""
        val text = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString() ?: ""

        Log.d("TrilhaCusto", "Título: $title | Texto: $text")


        val parsedData = NotificationParser.parse(text)

        if (parsedData != null) {
            val dto = TransacaoPluggyDto(
                id = UUID.randomUUID().toString(),
                amount = parsedData.valor,
                description = parsedData.estabelecimento,
                date = System.currentTimeMillis()
            )


            serviceScope.launch {
                try {
                    processarUseCase(listOf(dto))
                    Log.d("TrilhaCusto", "Sucesso! Compra no '${dto.description}' salva.")
                } catch (e: Exception) {
                    Log.e("TrilhaCusto", "Erro ao processar a notificação no UseCase", e)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        serviceScope.cancel()
    }
}
