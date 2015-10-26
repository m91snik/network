/**
 * Created by m91snik on 24.10.15.
 */
package com.m91snik.network;

import org.junit.Test;

public class NetworkNegativeTest {

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNetworkSize() {
        new NetworkImpl(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSelfConnect() {
        NetworkImpl network = new NetworkImpl(1);
        network.connect(1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConnectOutOfRangeFromLow() {
        NetworkImpl network = new NetworkImpl(1);
        network.connect(0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConnectOutOfRangeToHigh() {
        NetworkImpl network = new NetworkImpl(1);
        network.connect(1, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConnectOutOfRangeFromHigh() {
        NetworkImpl network = new NetworkImpl(1);
        network.connect(1, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConnectOutOfRangeToLow() {
        NetworkImpl network = new NetworkImpl(1);
        network.connect(1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testQueryOutOfRangeFromLow() {
        Network network = buildTwoNodesNetwork();
        network.query(0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testQueryOutOfRangeToLow() {
        Network network = buildTwoNodesNetwork();
        network.query(1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testQueryOutOfRangeFromHigh() {
        Network network = buildTwoNodesNetwork();
        network.query(3, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testQueryOutOfRangeToHigh() {
        Network network = buildTwoNodesNetwork();
        network.query(1, 3);
    }

    private Network buildTwoNodesNetwork() {
        NetworkImpl network = new NetworkImpl(2);
        network.connect(1, 2);
        return network;
    }

}
