/**
 * Created by m91snik on 24.10.15.
 */
package com.m91snik.network;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicReferenceArray;


/**
 * Network implementation which is based on {@link java.util.Set} as connections storage.
 * <p/>
 * It can be used in high-concurrent application because internally it's based on
 * {@link java.util.concurrent.atomic.AtomicReferenceArray} and {@link java.util.concurrent.ConcurrentSkipListSet}.
 * <p/>
 * It also does cache already performed queries to speed-up query process for repeatable requests.
 */
public class NetworkImpl implements Network {

    private static class Node {
        private final Set<Integer> connections;
        private final Set<Integer> cachedRoutes;

        public Node() {
            this.connections = new ConcurrentSkipListSet<>();
            this.cachedRoutes = new HashSet<>();
        }
    }

    // contains elements of network and their connections.
    // connection is an index of linked element.
    final AtomicReferenceArray<Node> network;

    /**
     * Initialize network with networkSize elements
     *
     * @param networkSize
     * @throws IllegalArgumentException if networkSize is zero or negative
     */
    public NetworkImpl(int networkSize) {
        if (networkSize <= 0) {
            throw new IllegalArgumentException("Network size should be positive number");
        }
        network = new AtomicReferenceArray<>(networkSize);
    }

    @Override
    public void connect(int elementA, int elementB) {
        if (elementA == elementB) {
            throw new IllegalArgumentException("From element should be different from To element");
        }
        validateRange(elementA);
        validateRange(elementB);

        connectInternal(elementA - 1, elementB - 1);
        connectInternal(elementB - 1, elementA - 1);
    }

    private void validateRange(int element) {
        if (element < 1 || element > network.length()) {
            throw new IllegalArgumentException("Element is out of [1," + network.length() + "] range");
        }
    }

    private void connectInternal(int fromIdx, int toIdx) {
        network.compareAndSet(fromIdx, null, new Node());
        network.get(fromIdx).connections.add(toIdx);
    }

    @Override
    public boolean query(int elementA, final int elementB) {
        validateRange(elementA);
        validateRange(elementB);

        if (elementA == elementB) {
            return true;
        }

        Set<Integer> visitedElements = new HashSet<>();
        int fromIdx = elementA - 1;
        int toIdx = elementB - 1;

        if (queryInternal(fromIdx, toIdx, visitedElements)) {
            cacheRoute(fromIdx, toIdx);
            return true;
        }
        return false;
    }


    /**
     * Recursive implementation of query route algorithm
     *
     * @param fromIdx         from element index
     * @param toIdx           to element index
     * @param visitedElements elements which were already visited. they are required to prevent searching in already passed path.
     * @return {@true} if route exists, {@false} if it doesn't
     */
    private boolean queryInternal(int fromIdx, int toIdx, Set<Integer> visitedElements) {
        Node nodeA = network.get(fromIdx);
        if (nodeA == null) {
            return false;
        }
        // if it's direct connection or it was already cached (i.e. passed, found before) than return true
        if (nodeA.connections.contains(toIdx) || nodeA.cachedRoutes.contains(toIdx)) {
            return true;
        }
        visitedElements.add(fromIdx);

        for (Object objConnection : nodeA.connections) {
            Integer connectionA = (Integer) objConnection;
            if (visitedElements.contains(connectionA)) {
                continue;
            }
            if (queryInternal(connectionA, toIdx, visitedElements)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Cache route from fromIdx to toIdx.
     *
     * @param fromIdx
     * @param toIdx
     */
    private void cacheRoute(int fromIdx, int toIdx) {
        Node fromNode = network.get(fromIdx);
        fromNode.cachedRoutes.add(toIdx);
    }
}
