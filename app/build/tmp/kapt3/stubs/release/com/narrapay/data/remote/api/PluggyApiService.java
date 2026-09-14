package com.narrapay.data.remote.api;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J2\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0003\u0010\u0007\u001a\u00020\b2\b\b\u0003\u0010\t\u001a\u00020\bH\u00a7@\u00a2\u0006\u0002\u0010\n\u00a8\u0006\u000b"}, d2 = {"Lcom/narrapay/data/remote/api/PluggyApiService;", "", "getTransactions", "Lretrofit2/Response;", "Lcom/narrapay/data/remote/dto/PluggyTransactionsResponse;", "accountId", "", "limit", "", "offset", "(Ljava/lang/String;IILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_release"})
public abstract interface PluggyApiService {
    
    @retrofit2.http.GET(value = "/transactions")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTransactions(@retrofit2.http.Query(value = "accountId")
    @org.jetbrains.annotations.NotNull()
    java.lang.String accountId, @retrofit2.http.Query(value = "limit")
    int limit, @retrofit2.http.Query(value = "offset")
    int offset, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.narrapay.data.remote.dto.PluggyTransactionsResponse>> $completion);
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
    }
}