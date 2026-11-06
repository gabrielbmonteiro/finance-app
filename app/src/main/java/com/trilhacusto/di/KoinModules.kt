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

val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE pessoas ADD COLUMN isFavorito INTEGER NOT NULL DEFAULT 0")
    }
}

val MIGRATION_5_6 = object : Migration(5, 6) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `faturas` (
                `mes` INTEGER NOT NULL,
                `ano` INTEGER NOT NULL,
                `diaFechamento` INTEGER NOT NULL,
                `diaVencimento` INTEGER NOT NULL,
                `inicioTimestamp` INTEGER NOT NULL,
                `fimTimestamp` INTEGER NOT NULL,
                `isFechada` INTEGER NOT NULL DEFAULT 0,
                `dataFechamentoEfetiva` INTEGER,
                PRIMARY KEY(`mes`, `ano`)
            )
            """.trimIndent()
        )
    }
}

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "TrilhaCusto_db"
        )
            .addMigrations(MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6)
            .fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                }

                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)

                    db.execSQL("INSERT OR REPLACE INTO configuracoes (id, diaFechamento, diaVencimento, isPrivacyModeEnabled) VALUES (1, 27, 5, 0)")

                    // Inicializa/assegura as faturas de Setembro (28/08 a 27/09), Outubro (28/09 a 27/10) e Novembro (28/10 a 27/11)
                    db.execSQL("INSERT OR REPLACE INTO faturas (mes, ano, diaFechamento, diaVencimento, inicioTimestamp, fimTimestamp, isFechada, dataFechamentoEfetiva) VALUES (8, 2026, 27, 5, 1787886000000, 1790564399999, 1, 1790564399999)")
                    db.execSQL("INSERT OR REPLACE INTO faturas (mes, ano, diaFechamento, diaVencimento, inicioTimestamp, fimTimestamp, isFechada, dataFechamentoEfetiva) VALUES (9, 2026, 27, 5, 1790564400000, 1793156399999, 1, 1793156399999)")
                    db.execSQL("INSERT OR REPLACE INTO faturas (mes, ano, diaFechamento, diaVencimento, inicioTimestamp, fimTimestamp, isFechada, dataFechamentoEfetiva) VALUES (10, 2026, 27, 5, 1793156400000, 1795834799999, 0, NULL)")

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
    single { get<AppDatabase>().faturaDao() }
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
    single { PluggyNetworkRepository(get(), get()) }
    single { com.trilhacusto.data.repository.UserPreferencesRepository(androidContext()) }
}

val domainModule = module {
    factory { AtribuirValoresUseCase(get()) }
    factory { ProcessarTransacaoPluggyUseCase(get()) }
}

val viewModelModule = module {
    viewModel {
        com.trilhacusto.ui.dashboard.DashboardViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
    viewModel { com.trilhacusto.ui.pendencias.PendenciasViewModel(get(), get()) }
    viewModel { com.trilhacusto.ui.rateio.AtribuicaoViewModel(get(), get(), get(), get()) }
    viewModel { com.trilhacusto.ui.transacao.TransacaoFormViewModel(get(), get()) }
    viewModel { com.trilhacusto.ui.extrato.ExtratoViewModel(get(), get(), get(), get()) }
    viewModel { com.trilhacusto.ui.ajustes.categorias.CategoriaViewModel(get()) }
    viewModel { com.trilhacusto.ui.ajustes.pessoas.PessoaViewModel(get(), get()) }
    viewModel { com.trilhacusto.ui.export.ExportViewModel(get(), get(), get(), get()) }
    viewModel { com.trilhacusto.ui.auth.AuthViewModel(get(), get()) }
    viewModel { com.trilhacusto.ui.auth.RegisterViewModel(get()) }
    viewModel { com.trilhacusto.ui.onboarding.OnboardingViewModel(get()) }
    viewModel { com.trilhacusto.ui.account.AccountViewModel(get()) }
}

val appModules =
    listOf(databaseModule, networkModule, repositoryModule, domainModule, viewModelModule)
