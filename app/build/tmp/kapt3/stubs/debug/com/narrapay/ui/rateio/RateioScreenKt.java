package com.narrapay.ui.rateio;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000R\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u001e\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0007\u001a(\u0010\u0006\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\fH\u0007\u001a4\u0010\r\u001a\u00020\u00012\u0006\u0010\u000e\u001a\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u00032\u0012\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\u00112\u0006\u0010\t\u001a\u00020\bH\u0007\u001a\u00f6\u0001\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00142\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\u0018\u0010\u0017\u001a\u0014\u0012\u0004\u0012\u00020\u0019\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\u00182\u0012\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\u00112\u0012\u0010\u001b\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\u00112\u0014\u0010\u001c\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u0019\u0012\u0004\u0012\u00020\u00010\u00112\u0012\u0010\u001d\u001a\u000e\u0012\u0004\u0012\u00020\u001e\u0012\u0004\u0012\u00020\u00010\u00112\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\u0012\u0010 \u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\u00112\u0018\u0010!\u001a\u0014\u0012\u0004\u0012\u00020\"\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\u00182\u0012\u0010#\u001a\u000e\u0012\u0004\u0012\u00020\"\u0012\u0004\u0012\u00020\u00010\u00112\f\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0007\u00a8\u0006%"}, d2 = {"AtalhoRateio", "", "texto", "", "onClick", "Lkotlin/Function0;", "FeedbackCard", "soma", "", "valorTotal", "diferenca", "isFechado", "", "RateioInputField", "label", "value", "onValueChange", "Lkotlin/Function1;", "RateioScreen", "uiState", "Lcom/narrapay/ui/rateio/RateioUiState;", "onBackClicked", "onConfirmarRateio", "onParteChanged", "Lkotlin/Function2;", "", "onTituloChanged", "onValorTotalChanged", "onCategoriaSelecionada", "onPessoaToggled", "Lcom/narrapay/data/local/entity/PessoaEntity;", "onClearError", "onAddCategoria", "onEditCategoria", "Lcom/narrapay/data/local/entity/CategoriaEntity;", "onRemoveCategoria", "onNavigateToPessoas", "app_debug"})
public final class RateioScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void RateioScreen(@org.jetbrains.annotations.NotNull()
    com.narrapay.ui.rateio.RateioUiState uiState, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBackClicked, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onConfirmarRateio, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function2<? super java.lang.Long, ? super java.lang.String, kotlin.Unit> onParteChanged, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onTituloChanged, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onValorTotalChanged, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onCategoriaSelecionada, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.narrapay.data.local.entity.PessoaEntity, kotlin.Unit> onPessoaToggled, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClearError, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onAddCategoria, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function2<? super com.narrapay.data.local.entity.CategoriaEntity, ? super java.lang.String, kotlin.Unit> onEditCategoria, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.narrapay.data.local.entity.CategoriaEntity, kotlin.Unit> onRemoveCategoria, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onNavigateToPessoas) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void RateioInputField(@org.jetbrains.annotations.NotNull()
    java.lang.String label, @org.jetbrains.annotations.NotNull()
    java.lang.String value, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onValueChange, double valorTotal) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void AtalhoRateio(@org.jetbrains.annotations.NotNull()
    java.lang.String texto, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void FeedbackCard(double soma, double valorTotal, double diferenca, boolean isFechado) {
    }
}