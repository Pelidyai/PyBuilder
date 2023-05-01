package com.pickaim.python_builder.action_tree.listeners;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

public class PreAction<T> {

    public static final int FAIL = 1;
    public static final int OK = 0;

    private final Callable<T> myTask;

    public PreAction(Callable<T> task) {
        myTask = task;
    }

    public T run() throws ExecutionException, InterruptedException {
        RunnableFuture<T> future = new FutureTask<>(myTask);
        future.run();
        return future.get();
    }
}
