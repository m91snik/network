package com.m91snik.network;

/**
 * Created by m91snik on 24.10.15.
 */
public interface Network {

    /**
     * Connects two elements in network
     *
     * @param elementA from element between 1 and to
     * @param elementB to element between from and network size
     */
    void connect(int elementA, int elementB);

    /**
     * Gets two elements in network and determines if there is a route between them
     *
     * @param from from element
     * @param to   to element
     * @return {@true} if there is a route between {@from} and {@to} and {@false} otherwise
     */
    boolean query(int from, int to);
}
