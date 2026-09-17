# Adicione regras customizadas do Proguard/R8 aqui.
# Por padrão, os componentes do Android (Activities, Services, etc) já são protegidos de ofuscação pelo plugin do Android.
# Se estiver usando Retrofit com Gson, mantenha os DTOs que mapeiam JSON:

-keep class com.narrapay.data.remote.dto.** { *; }

# Mantém as classes do Room
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**
