/**
 * Created by m91snik on 24.10.15.
 */
package com.m91snik.network;


/**
 * Network consists of nodes which can be connected with each other.
 * Use {@link #connect(int, int) connect} method to connect nodes in network passing elements to it.
 * Use {@link #query(int, int) query} method to determine if elements are connected in network
 */
public interface Network {

    /**
     * Connects two elements in network
     *
     * @param elementA from element between 1 and to
     * @param elementB to element between from and network size
     * @throws IllegalArgumentException if elementA is equal to elementB or they are out of range
     */
    void connect(int elementA, int elementB);

    /**
     * Gets two elements in network and determines if there is a route between them
     *
     * @param elementA from element
     * @param elementB to element
     * @return {@true} if there is a route between {@from} and {@to} and {@false} otherwise
     * @throws IllegalArgumentException if elementA or elementB are out of range
     */
    boolean query(int elementA, int elementB);
}
