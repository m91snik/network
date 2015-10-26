/**
 * Created by m91snik on 24.10.15.
 */
package com.m91snik.network;

import org.junit.Assert;
import org.junit.Test;

public class NetworkPositiveTest {

    @Test
    public void test1ElementsNetwork() {
        Network network = new NetworkImpl(1);
        Assert.assertTrue(network.query(1, 1));
    }

    @Test
    public void test2ElementsNetwork() {
        Network network = new NetworkImpl(2);
        network.connect(1, 2);
        Assert.assertTrue(network.query(1, 2));
        Assert.assertTrue(network.query(2, 1));
    }

    @Test
    public void testLoopNetwork() {
        Network network = new NetworkImpl(4);
        network.connect(1, 2);
        network.connect(2, 3);
        network.connect(3, 4);
        network.connect(4, 1);
        network.connect(2, 4);
        Assert.assertTrue(network.query(1, 4));
        Assert.assertTrue(network.query(2, 1));
    }

    @Test
    public void testNetwork() {
        Network network = buildComplexNetwork();
        Assert.assertTrue(network.query(1, 4));
        Assert.assertTrue(network.query(6, 4));
        Assert.assertTrue(network.query(4, 1));
        Assert.assertTrue(network.query(1, 5));
        Assert.assertTrue(network.query(6, 5));
        Assert.assertFalse(network.query(1, 7));
        Assert.assertFalse(network.query(3, 8));
    }

    // specific simple test to cover route caching feature. it proves that caching works in case when
    // we call 2 queries with the same args
    @Test
    public void testCachedRoutesNetwork() {
        Network network = buildComplexNetwork();
        long startTime = System.nanoTime();
        Assert.assertTrue(network.query(1, 5));
        long unCachedTime = System.nanoTime() - startTime;
        startTime = System.nanoTime();
        Assert.assertTrue(network.query(1, 5));
        long cachedTime = System.nanoTime() - startTime;
        System.out.println("Uncached " + unCachedTime);
        System.out.println("Cached " + cachedTime);
        Assert.assertTrue(unCachedTime > cachedTime);
    }

    private Network buildComplexNetwork() {
        Network network = new NetworkImpl(8);
        network.connect(1, 2);
        network.connect(1, 6);
        network.connect(2, 4);
        network.connect(5, 8);
        network.connect(8, 6);
        return network;
    }
}
