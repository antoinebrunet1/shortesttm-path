package com.example.shortesttmpath.data;

/**
 * Represents the edge in a graph.
 *
 * @param destination The destination node.
 * @param distance The distance between the source node and the destination node.
 */
public record Edge(int destination, int distance) {
}
