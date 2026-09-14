package com.narrapay.data.remote.api;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J\u001e\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u00a7@\u00a2\u0006\u0002\u0010\u0007J(\u0010\b\u001a\b\u0012\u0004\u0012\u00020\t0\u00032\b\b\u0001\u0010\n\u001a\u00020\u000b2\b\b\u0001\u0010\f\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\r\u00a8\u0006\u000e"}, d2 = {"Lcom/narrapay/data/remote/api/PluggyApiService;", "", "auth", "Lretrofit2/Response;", "Lcom/narrapay/data/remote/dto/PluggyAuthResponse;", "request", "Lcom/narrapay/data/remote/dto/PluggyAuthRequest;", "(Lcom/narrapay/data/remote/dto/PluggyAuthRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getTransactions", "Lcom/narrapay/data/remote/dto/PluggyTransactionsResponse;", "apiKey", "", "accountId", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface PluggyApiService {
    
    @retrofit2.http.POST(value = "/auth")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object auth(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.narrapay.data.remote.dto.PluggyAuthRequest request, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.narrapay.data.remote.dto.PluggyAuthResponse>> $completion);
    
    @retrofit2.http.GET(value = "/v2/transactions")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTransactions(@retrofit2.http.Header(value = "X-API-KEY")
    @org.jetbrains.annotations.NotNull()
    java.lang.String apiKey, @retrofit2.http.Query(value = "accountId")
    @org.jetbrains.annotations.NotNull()
    java.lang.String accountId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.narrapay.data.remote.dto.PluggyTransactionsResponse>> $completion);
}