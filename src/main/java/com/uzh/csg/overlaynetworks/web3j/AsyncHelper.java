package com.uzh.csg.overlaynetworks.web3j;

import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.Response;

import java.io.IOException;
import java.util.concurrent.Future;

/**
 * Created by Christian Killer on 12.04.2017
 */


public final class AsyncHelper {

    private AsyncHelper() {
        throw new IllegalAccessError();
    }

    /**
     * Waits for the result of an asynchronous blockchain request.
     *
     * @param request an asynchronous blockchain request
     * @param <T> response type
     * @return A new Response instance
     * @throws IllegalStateException if something unexpected happens (like an IOException)
     */
    
    public static <T extends Response<?>> T waitForResult(Request<?, T> request) {
        try {
            return request.send();
        } catch (IOException x) {
            throw new IllegalStateException("Error retrieving the async result from the blockchain.", x);
        }
    }

    /**
     * Waits for the result of an asynchronous Future computation.
     *
     * @param future a Future instance
     * @param <T> result type
     * @return The resulting value of the Future computation
     * @throws IllegalStateException if something unexpected happens (like an IOException)
     */
    
    public static <T> T waitForResult(Future<? extends T> future) {
        try {
            return future.get();
        } catch (Exception x) {
            throw new IllegalStateException("Error retrieving the async result from the blockchain.", x);
        }
    }

}