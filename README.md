# ??????

?????? é um aplicativo financeiro de gerenciamento de despesas, rateio e pendências construído para a plataforma Android, focado em trazer uma experiência fluida para acompanhamento de gastos coletivos e individuais.

## ✨ Funcionalidades
- **Dashboard Interativo**: Visão geral dos gastos por mês com gráficos em anel dinâmicos.
- **Controle de Pendências**: Lista de despesas pendentes com busca em tempo real. Permite editar, ratear, desativar (ignorar) ou reativar pendências com um clique.
- **Sistema de Rateio Avançado**: Atribuição flexível e inteligente de valores pagos por diferentes pessoas em uma única despesa.
- **Captura de Transações**: Serviço de leitura de notificações capaz de extrair automaticamente os valores gastos do banco (através do `TransacaoNotificationService`).
- **Armazenamento Offline**: Utiliza banco de dados local para manter todos os extratos e configurações 100% seguros e disponíveis sem internet.

## 🛠 Tecnologias e Arquitetura
- **Kotlin** & **Jetpack Compose**: Interface de usuário 100% declarativa, moderna e reativa.
- **MVVM**: Arquitetura orientada a estados (*StateFlow*).
- **Room Database**: Persistência de dados locais relacionais.
- **Coroutines & Flow**: Processamento assíncrono de ponta a ponta.
- **Koin**: Injeção de dependências leve e rápida.

## 🚀 Como Executar
1. Clone este repositório:
   ```bash
   git clone https://github.com/gabrielbmonteiro/finance-app.git
   ```
2. Abra o diretório do projeto no **Android Studio**.
3. Crie um arquivo chamado `local.properties` na raiz do projeto e adicione suas credenciais da API da Pluggy (que podem ser obtidas no dashboard do [meu.pluggy.ai](https://meu.pluggy.ai)):
   ```properties
   PLUGGY_CLIENT_ID=seu_client_id_aqui
   PLUGGY_CLIENT_SECRET=seu_client_secret_aqui
   PLUGGY_ACCOUNT_ID=seu_account_id_aqui
   ```
4. Aguarde o *Gradle Sync* finalizar para baixar todas as dependências e reconhecer as variáveis.
5. Execute o projeto (`Shift + F10`) em um emulador ou dispositivo físico rodando Android.

---
*Desenvolvido na versão Beta de desenvolvimento.*
