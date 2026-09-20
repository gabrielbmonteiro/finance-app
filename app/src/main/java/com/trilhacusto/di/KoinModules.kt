package com.trilhacusto.di

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.trilhacusto.data.local.AppDatabase
import com.trilhacusto.data.remote.api.PluggyApiService
import com.trilhacusto.data.remote.repository.PluggyNetworkRepository
import com.trilhacusto.data.repository.TransacaoRepositoryImpl
import com.trilhacusto.domain.repository.TransacaoRepository
import com.trilhacusto.domain.usecase.AtribuirValoresUseCase
import com.trilhacusto.domain.usecase.ProcessarTransacaoPluggyUseCase
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE configuracoes ADD COLUMN isPrivacyModeEnabled INTEGER NOT NULL DEFAULT 0")
    }
}

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "TrilhaCusto_db"
        )
        .addMigrations(MIGRATION_3_4)
        .fallbackToDestructiveMigration()
        .addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
            }
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)

                db.execSQL("INSERT OR IGNORE INTO configuracoes (id, diaFechamento, diaVencimento, isPrivacyModeEnabled) VALUES (1, 25, 5, 0)")


                db.execSQL("INSERT OR IGNORE INTO categorias (id, nome, icone, corHex) VALUES (1, 'Transporte', '🚗', '#FF9800')")
                db.execSQL("INSERT OR IGNORE INTO categorias (id, nome, icone, corHex) VALUES (2, 'Alimentação', '🍔', '#F44336')")
                db.execSQL("INSERT OR IGNORE INTO categorias (id, nome, icone, corHex) VALUES (3, 'Gasolina', '⛽', '#607D8B')")
                db.execSQL("INSERT OR IGNORE INTO categorias (id, nome, icone, corHex) VALUES (4, 'Presente', '🎁', '#E91E63')")
                db.execSQL("INSERT OR IGNORE INTO categorias (id, nome, icone, corHex) VALUES (5, 'Saúde', '🏥', '#4CAF50')")
                db.execSQL("INSERT OR IGNORE INTO categorias (id, nome, icone, corHex) VALUES (6, 'Moradia', '🏠', '#795548')")
                db.execSQL("INSERT OR IGNORE INTO categorias (id, nome, icone, corHex) VALUES (7, 'Outros', '📦', '#9E9E9E')")
            }
        }).build()
    }
    
    single { get<AppDatabase>().transacaoDao() }
    single { get<AppDatabase>().pessoaDao() }
    single { get<AppDatabase>().categoriaDao() }
    single { get<AppDatabase>().regraAutomacaoDao() }
    single { get<AppDatabase>().configuracoesDao() }
}

val networkModule = module {
    single {
        OkHttpClient.Builder().build()
    }
    
    single {
        Retrofit.Builder()
            .baseUrl("https://api.pluggy.ai")
            .client(get<OkHttpClient>())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PluggyApiService::class.java)
    }
}

val repositoryModule = module {
    single<TransacaoRepository> { TransacaoRepositoryImpl(get(), get(), get(), get()) }
    single { PluggyNetworkRepository(get(), get()) } // We will update PluggyNetworkRepository next
    single { com.trilhacusto.data.repository.UserPreferencesRepository(androidContext()) }
}

val domainModule = module {
    factory { AtribuirValoresUseCase(get()) }
    factory { ProcessarTransacaoPluggyUseCase(get()) }
}

val viewModelModule = module {
    viewModel { com.trilhacusto.ui.dashboard.DashboardViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { com.trilhacusto.ui.pendencias.PendenciasViewModel(get(), get()) }
    viewModel { com.trilhacusto.ui.rateio.AtribuicaoViewModel(get(), get(), get(), get()) }
    viewModel { com.trilhacusto.ui.transacao.TransacaoFormViewModel(get(), get()) }
    viewModel { com.trilhacusto.ui.extrato.ExtratoViewModel(get(), get(), get()) }
    viewModel { com.trilhacusto.ui.ajustes.categorias.CategoriaViewModel(get()) }
    viewModel { com.trilhacusto.ui.ajustes.pessoas.PessoaViewModel(get(), get()) }
    viewModel { com.trilhacusto.ui.export.ExportViewModel(get(), get(), get()) }
    viewModel { com.trilhacusto.ui.auth.AuthViewModel(get(), get()) }
    viewModel { com.trilhacusto.ui.auth.RegisterViewModel(get()) }
    viewModel { com.trilhacusto.ui.onboarding.OnboardingViewModel(get()) }
    viewModel { com.trilhacusto.ui.account.AccountViewModel(get()) }
}

val appModules = listOf(databaseModule, networkModule, repositoryModule, domainModule, viewModelModule)
