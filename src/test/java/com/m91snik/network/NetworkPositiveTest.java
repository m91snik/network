package com.m91snik.network;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by m91snik on 24.10.15.
 */
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
    public void testNetwork() {
        Network network = buildComplexNetwork();
        Assert.assertTrue(network.query(1, 4));
        Assert.assertTrue(network.query(6, 4));
        Assert.assertTrue(network.query(4, 1));
        Assert.assertTrue(network.query(1, 5));
        Assert.assertTrue(network.query(6, 5));
    }

    @Test
    public void testCachedRoutesNetwork() {
        Network network = buildComplexNetwork();
        Assert.assertTrue(network.query(1, 5));
        Assert.assertTrue(network.query(6, 5));
        Assert.assertTrue(network.query(1, 5));
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
