package com.dc.cache.raft;

import com.alipay.sofa.jraft.Closure;
import com.alipay.sofa.jraft.Status;
import com.turing.rpc.Response;
import lombok.Setter;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class ResponseClosure implements Closure {

    @Setter
    private volatile Response response;

    @Setter
    private volatile Throwable throwable;

    private final CompletableFuture<Response> future = new CompletableFuture<>();

    public ResponseClosure() {

    }

    @Override
    public void run(Status status) {
        if (status.isOk()) {
            future.complete(response);
            return;
        }

        final Throwable throwable = this.throwable;
        future.completeExceptionally(Objects.nonNull(throwable) ? new RuntimeException(throwable.toString())
                : new RuntimeException("operation failure"));
    }

    public CompletableFuture<Response> toFuture () {
        return future;
    }
}
