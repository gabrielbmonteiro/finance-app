# TrilhaCusto

O **TrilhaCusto** é um aplicativo financeiro Android de alto nível focado em gestão de despesas, rateios e pendências. Construído com uma arquitetura robusta e focado em uma experiência premium, ele traz uma fluidez impecável para o acompanhamento de gastos coletivos e individuais.

## ✨ Funcionalidades
- **Dashboard Interativo**: Visão geral do histórico de gastos com gráficos em anel dinâmicos e filtros de data em tempo real.
- **Sistema de Rateio Avançado**: Atribuição flexível e inteligente de valores pagos por diferentes pessoas em uma única despesa.
- **Controle de Pendências**: Lista de despesas não categorizadas ou incompletas. Permite editar, ratear, ignorar ou classificar pendências com um clique.
- **Leitura Passiva de Transações**: Serviço de captura de notificações em background capaz de extrair automaticamente os valores gastos do banco (via `TransacaoNotificationService`).
- **Segurança e Privacidade Offline**: Banco de dados criptografável que garante que 100% do seu histórico financeiro fique restrito ao seu celular, sem depender de servidores de terceiros.

## 🛠 Tecnologias e Arquitetura Avançada
- **Kotlin & Jetpack Compose**: UI 100% declarativa.
- **MVVM + StateFlow (Lifecycle-Aware)**: O aplicativo utiliza `collectAsStateWithLifecycle` para "congelar" fluxos do banco de dados quando minimizado, otimizando o consumo de bateria ao extremo.
- **Room Database com KSP**: Persistência de dados locais ultra-rápida (substituindo o antigo Kapt para maior velocidade de compilação).
- **Coroutines & Flow**: Processamento assíncrono.
- **Koin**: Injeção de dependências modular e leve.
- **R8 Minify**: Configurado nativamente para ofuscação de código e redução do tamanho do APK para ambiente de produção.

## 🚀 Como Executar
1. Clone este repositório:
   ```bash
   git clone https://github.com/gabrielbmonteiro/finance-app.git
   ```
2. Abra o diretório do projeto no **Android Studio**.
3. Aguarde o *Gradle Sync* finalizar para baixar todas as dependências (Koin, Retrofit, Room, etc.).
4. Execute o projeto em um emulador ou dispositivo físico rodando Android.
5. Crie sua conta local e configure suas chaves da API da Pluggy de forma segura diretamente dentro do próprio aplicativo!