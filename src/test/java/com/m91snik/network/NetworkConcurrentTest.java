/**
 * Created by m91snik on 24.10.15.
 */
package com.m91snik.network;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class NetworkConcurrentTest {

    /**
     * Tests {@link com.m91snik.network.Network#connect(int, int) connect} network method under load.
     */
    @Test
    public void testConcurrentNetworkConnect() throws InterruptedException {
        int networkSize = 100;
        final CountDownLatch finishLatch = new CountDownLatch(networkSize - 1);
        final Network network = new NetworkImpl(networkSize);
        connectElementsConcurrently(network, networkSize, finishLatch);

        finishLatch.await();

        assertConnections(network);

    }

    private Network connectElementsConcurrently(final Network network, int networkSize, final CountDownLatch finishLatch) {
        ExecutorService executorService = Executors.newFixedThreadPool(networkSize - 1);
        final CountDownLatch startLatch = new CountDownLatch(networkSize - 1);
        for (int i = 2; i <= 100; i++) {
            final int elementB = i;
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    startLatch.countDown();
                    try {
                        startLatch.await();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new IllegalStateException(e);
                    }
                    network.connect(1, elementB);
                    finishLatch.countDown();
                }
            });
        }
        return network;
    }

    private void assertConnections(Network network) {
        for (int i = 2; i <= 100; i++) {
            Assert.assertTrue(network.query(1, i));
        }
    }

    /**
     * Tests {@link com.m91snik.network.Network#query(int, int) query} network method under load.
     */
    @Test
    public void testConcurrentNetworkQuery() throws InterruptedException {
        final Network network = new NetworkImpl(4);
        network.connect(1, 2);
        network.connect(2, 3);
        network.connect(1, 4);

        int poolSize = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(poolSize);

        final CountDownLatch startLatch = new CountDownLatch(poolSize);
        final CountDownLatch finishLatch = new CountDownLatch(poolSize);
        for (int i = 0; i < poolSize; i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    startLatch.countDown();
                    try {
                        startLatch.await();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new IllegalStateException(e);
                    }
                    Assert.assertTrue(network.query(((int) Math.random() * 10) % 4 + 1, ((int) Math.random() * 10) % 4 + 1));
                    finishLatch.countDown();
                }
            });
        }

        finishLatch.await();
    }

}
