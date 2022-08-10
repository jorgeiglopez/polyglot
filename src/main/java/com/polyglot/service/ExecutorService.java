package com.polyglot.service;

import java.util.concurrent.Executors;

public class ExecutorService {

    private static final int THREAD_LIMIT = 1;

    private static final java.util.concurrent.ExecutorService executorService = Executors.newFixedThreadPool(THREAD_LIMIT);

}
