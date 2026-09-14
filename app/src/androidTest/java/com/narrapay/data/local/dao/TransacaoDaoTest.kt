package com.narrapay.data.local.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.narrapay.data.local.entity.AtribuicaoTransacaoEntity
import com.narrapay.data.local.entity.CategoriaEntity
import com.narrapay.data.local.entity.PessoaEntity
import com.narrapay.data.local.entity.RegraAutomacaoEntity
import com.narrapay.data.local.entity.TransacaoEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

// Banco de dados em memória exclusivo para testes
@Database(
    entities = [
        PessoaEntity::class,
        CategoriaEntity::class,
        TransacaoEntity::class,
        AtribuicaoTransacaoEntity::class,
        RegraAutomacaoEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class TestAppDatabase : RoomDatabase() {
    abstract fun transacaoDao(): TransacaoDao
    abstract fun pessoaDao(): PessoaDao
    abstract fun categoriaDao(): CategoriaDao
}

@RunWith(AndroidJUnit4::class)
class TransacaoDaoTest {

    private lateinit var db: TestAppDatabase
    private lateinit var transacaoDao: TransacaoDao
    private lateinit var pessoaDao: PessoaDao
    private lateinit var categoriaDao: CategoriaDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        
        // Criando banco de dados na memória
        db = Room.inMemoryDatabaseBuilder(
            context, TestAppDatabase::class.java
        ).allowMainThreadQueries().build()
        
        transacaoDao = db.transacaoDao()
        pessoaDao = db.pessoaDao()
        categoriaDao = db.categoriaDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun inserir_transacao_completa_e_ler_relacionamentos_corretamente() = runBlocking {
        // 1. Inserir Entidades Fortes (Foreign Keys)
        val pessoaId = pessoaDao.insert(
            PessoaEntity(nome = "João", corHex = "#FF0000")
        )
        
        val categoriaId = categoriaDao.insert(
            CategoriaEntity(nome = "Alimentação", icone = "🍔", corHex = "#00FF00")
        )
        
        // 2. Inserir Transacao vinculada à Categoria
        val transacaoId = "txn_999"
        val transacao = TransacaoEntity(
            id = transacaoId,
            valorTotal = 200.0,
            descricaoOriginal = "Supermercado",
            dataHora = 1690000000L,
            categoriaId = categoriaId,
            statusAtribuicao = "PENDENTE"
        )
        transacaoDao.insertTransacao(transacao)
        
        // 3. Inserir Atribuicao vinculada à Transação e à Pessoa
        val atribuicao = AtribuicaoTransacaoEntity(
            transacaoId = transacaoId,
            pessoaId = pessoaId,
            valorAtribuido = 200.0
        )
        transacaoDao.insertAtribuicoes(listOf(atribuicao))
        
        // 4. Buscar Transação com Relacionamentos Mapeados
        val transacoesPendentes = transacaoDao.getTransacoesPendentes().first()
        
        // 5. Asserts
        assertTrue(transacoesPendentes.isNotEmpty())
        
        val transacaoCompleta = transacoesPendentes.first { it.transacao.id == transacaoId }
        
        // Validando transação
        assertEquals("Supermercado", transacaoCompleta.transacao.descricaoOriginal)
        
        // Validando Relation de Categoria
        assertNotNull(transacaoCompleta.categoria)
        assertEquals("Alimentação", transacaoCompleta.categoria?.nome)
        
        // Validando Relation de Atribuições com Pessoa
        assertEquals(1, transacaoCompleta.atribuicoes.size)
        
        val atribuicaoComPessoa = transacaoCompleta.atribuicoes.first()
        assertEquals(200.0, atribuicaoComPessoa.atribuicao.valorAtribuido, 0.0)
        assertEquals("João", atribuicaoComPessoa.pessoa.nome)
    }
}
