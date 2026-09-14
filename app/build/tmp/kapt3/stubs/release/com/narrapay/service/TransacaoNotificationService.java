package com.narrapay.service;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\r\u001a\u00020\u000eH\u0016J\u0012\u0010\u000f\u001a\u00020\u000e2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2 = {"Lcom/narrapay/service/TransacaoNotificationService;", "Landroid/service/notification/NotificationListenerService;", "()V", "job", "Lkotlinx/coroutines/CompletableJob;", "processarUseCase", "Lcom/narrapay/domain/usecase/ProcessarTransacaoPluggyUseCase;", "getProcessarUseCase", "()Lcom/narrapay/domain/usecase/ProcessarTransacaoPluggyUseCase;", "setProcessarUseCase", "(Lcom/narrapay/domain/usecase/ProcessarTransacaoPluggyUseCase;)V", "serviceScope", "Lkotlinx/coroutines/CoroutineScope;", "onDestroy", "", "onNotificationPosted", "sbn", "Landroid/service/notification/StatusBarNotification;", "app_release"})
public final class TransacaoNotificationService extends android.service.notification.NotificationListenerService {
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CompletableJob job = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope serviceScope = null;
    public com.narrapay.domain.usecase.ProcessarTransacaoPluggyUseCase processarUseCase;
    
    public TransacaoNotificationService() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.narrapay.domain.usecase.ProcessarTransacaoPluggyUseCase getProcessarUseCase() {
        return null;
    }
    
    public final void setProcessarUseCase(@org.jetbrains.annotations.NotNull()
    com.narrapay.domain.usecase.ProcessarTransacaoPluggyUseCase p0) {
    }
    
    @java.lang.Override()
    public void onNotificationPosted(@org.jetbrains.annotations.Nullable()
    android.service.notification.StatusBarNotification sbn) {
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
}