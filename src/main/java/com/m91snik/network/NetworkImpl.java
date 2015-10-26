package com.m91snik.network;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by m91snik on 24.10.15.
 */
public class NetworkImpl implements Network {

    private static class Node {
        private final Set<Integer> connections;
        private final Set<Integer> cachedRoutes;

        public Node() {
            this.connections = new HashSet<>();
            this.cachedRoutes = new HashSet<>();
        }
    }

    // contains elements of network and their connections.
    // connection is an index of linked element.
    final Node[] network;

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
        network = new Node[networkSize];
    }

    @Override
    public void connect(int elementA, int elementB) {
        if (elementA == elementB) {
            throw new IllegalArgumentException("From element should be different from To element");
        }
        connectInternal(elementA - 1, elementB - 1);
        connectInternal(elementB - 1, elementA - 1);
    }

    private void connectInternal(int fromIdx, int toIdx) {
        Node node = network[fromIdx];
        if (node == null) {
            network[fromIdx] = node = new Node();
        }
        node.connections.add(toIdx);
    }

    @Override
    public boolean query(int elementA, final int elementB) {
        Set<Integer> visitedElements = new HashSet<>();
        if (elementA == elementB) {
            return true;
        }

        if (queryInternal(elementA - 1, elementB - 1, visitedElements)) {
            Node nodeA = network[elementA - 1];
            nodeA.cachedRoutes.add(elementB - 1);
            return true;
        }
        return false;
    }

    private boolean queryInternal(int fromIdx, int toIdx, Set<Integer> visitedElements) {
        Node nodeA = network[fromIdx];
        if (nodeA == null) {
            return false;
        }
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
}
