package com.m91snik.network;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by m91snik on 24.10.15.
 */
public class NetworkNegativeTest {

    @Test
    public void test2Nodes() {
        Network network = new NetworkImpl(2);
        network.connect(1, 2);
        Assert.assertTrue(network.query(1, 2));
    }
}
