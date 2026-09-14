package com.narrapay.di

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.narrapay.data.local.AppDatabase
import com.narrapay.data.remote.api.PluggyApiService
import com.narrapay.data.remote.interceptor.AuthInterceptor
import com.narrapay.data.remote.repository.PluggyNetworkRepository
import com.narrapay.data.repository.TransacaoRepositoryImpl
import com.narrapay.domain.repository.TransacaoRepository
import com.narrapay.domain.usecase.AtribuirValoresUseCase
import com.narrapay.domain.usecase.ProcessarTransacaoPluggyUseCase
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "narrapay_db"
        )
        .fallbackToDestructiveMigration()
        .addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                db.execSQL("INSERT INTO pessoas (id, nome, corHex) VALUES (1, 'Gabriel', '#4CAF50')")
                db.execSQL("INSERT INTO pessoas (id, nome, corHex) VALUES (2, 'Pai', '#FF9800')")
            }
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)

                db.execSQL("INSERT OR IGNORE INTO pessoas (id, nome, corHex) VALUES (1, 'Gabriel', '#4CAF50')")
                db.execSQL("INSERT OR IGNORE INTO pessoas (id, nome, corHex) VALUES (2, 'Pai', '#FF9800')")


                db.execSQL("INSERT OR IGNORE INTO configuracoes (id, diaFechamento, diaVencimento) VALUES (1, 25, 5)")


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
    single { PluggyNetworkRepository(get()) }
}

val domainModule = module {
    factory { AtribuirValoresUseCase(get()) }
    factory { ProcessarTransacaoPluggyUseCase(get()) }
}

val viewModelModule = module {
    viewModel { com.narrapay.ui.dashboard.DashboardViewModel(get(), get(), get(), get(), get()) }
    viewModel { com.narrapay.ui.pendencias.PendenciasViewModel(get()) }
    viewModel { com.narrapay.ui.rateio.AtribuicaoViewModel(get(), get(), get(), get()) }
    viewModel { com.narrapay.ui.transacao.TransacaoFormViewModel(get()) }
    viewModel { com.narrapay.ui.extrato.ExtratoViewModel(get(), get()) }
    viewModel { com.narrapay.ui.ajustes.categorias.CategoriaViewModel(get()) }
    viewModel { com.narrapay.ui.ajustes.pessoas.PessoaViewModel(get()) }
}

val appModules = listOf(databaseModule, networkModule, repositoryModule, domainModule, viewModelModule)
