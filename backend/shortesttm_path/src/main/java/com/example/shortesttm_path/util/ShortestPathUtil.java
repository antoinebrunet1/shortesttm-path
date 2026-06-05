package com.example.shortesttm_path.util;

import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ShortestPathUtil {
    private static final String LINES_FILES_PARENT_FOLDER_PATH = "backend/shortesttm_path/src/main/resources/static/";
    private static final List<String> LINES_FILES_NAMES = Arrays.asList(
            "blue_line_stations.txt",
            "green_line_stations.txt",
            "orange_line_stations.txt",
            "yellow_line_stations.txt"
    );
    private static final List<List<Integer>> GRAPH;
    private static final Map<String, Integer> STATIONS_NAMES_TO_INTS;

    static {
        try {
            STATIONS_NAMES_TO_INTS = getStationsNamesToInts();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            GRAPH = getGraph();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final Map<Integer, String> INTS_TO_STATIONS_NAMES = getIntsToStationsNames();
    private static final int NUMBER_OF_VERTICES = 68;

    public static void printShortestPath(String startStation, String destinationStation) {
        int S = STATIONS_NAMES_TO_INTS.get(startStation);
        int D = STATIONS_NAMES_TO_INTS.get(destinationStation);
        // par[] array stores the parent of nodes
        List<Integer> par
                = new ArrayList<>(Collections.nCopies(NUMBER_OF_VERTICES, -1));

        // dist[] array stores the distance of nodes from S
        List<Integer> dist = new ArrayList<>(
                Collections.nCopies(NUMBER_OF_VERTICES, Integer.MAX_VALUE));

        // Function call to find the distance of all nodes
        // and their parent nodes
        bfs(GRAPH, S, par, dist);

        if (dist.get(D) == Integer.MAX_VALUE) {
            System.out.println(
                    "Source and Destination are not connected");
            return;
        }

        // List path stores the shortest path
        List<Integer> path = new ArrayList<>();
        int currentNode = D;
        path.add(D);
        while (par.get(currentNode) != -1) {
            path.add(par.get(currentNode));
            currentNode = par.get(currentNode);
        }

        // Printing path from source to destination
        for (int i = path.size() - 1; i >= 0; i--)
            System.out.print(INTS_TO_STATIONS_NAMES.get(path.get(i)) + " ");
    }

    // Modified bfs to store the parent of nodes along with
    // the distance from the source node
    static void bfs(List<List<Integer> > graph, int S,
                    List<Integer> par, List<Integer> dist)
    {
        // Queue to store the nodes in the order they are
        // visited
        Queue<Integer> q = new LinkedList<>();
        // Mark the distance of the source node as 0
        dist.set(S, 0);
        // Push the source node to the queue
        q.add(S);

        // Iterate until the queue is not empty
        while (!q.isEmpty()) {
            // Pop the node at the front of the queue
            int node = q.poll();

            // Explore all the neighbors of the current node
            for (int neighbor : graph.get(node)) {
                // Check if the neighboring node is not
                // visited
                if (dist.get(neighbor)
                        == Integer.MAX_VALUE) {
                    // Mark the current node as the parent
                    // of the neighboring node
                    par.set(neighbor, node);
                    // Mark the distance of the neighboring
                    // node as the distance of the current
                    // node + 1
                    dist.set(neighbor, dist.get(node) + 1);
                    // Insert the neighboring node to the
                    // queue
                    q.add(neighbor);
                }
            }
        }
    }

    private static Map<Integer, String> getIntsToStationsNames() {
        Map<Integer, String> intsToStationsNames = new LinkedHashMap<>();

        for (String station : STATIONS_NAMES_TO_INTS.keySet()) {
            intsToStationsNames.put(STATIONS_NAMES_TO_INTS.get(station), station);
        }

        return intsToStationsNames;
    }

    private static Map<String, Integer> getStationsNamesToInts() throws IOException {
        Map<String, Integer> stationNamesToInts = new LinkedHashMap<>();
        Set<String> uniqueStationsNames = getUniqueStationsNames();
        int i = 0;

        for (String stationName : uniqueStationsNames) {
            stationNamesToInts.put(stationName, i);
            i++;
        }

        return stationNamesToInts;
    }

    private static Set<String> getUniqueStationsNames() throws IOException {
        Set<String> uniqueStationsNames = new LinkedHashSet<>();

        for (String line_file_name : LINES_FILES_NAMES) {
            addLineToUniqueStationsNames(uniqueStationsNames, line_file_name);
        }

        return uniqueStationsNames;
    }

    private static void addLineToUniqueStationsNames(Set<String> uniqueStationsNames, String line_file_name) throws IOException {
        Path filePath = Paths.get(LINES_FILES_PARENT_FOLDER_PATH + line_file_name);
        List<String> stations = Files.readAllLines(filePath);

        uniqueStationsNames.addAll(stations);
    }

    private static List<List<Integer>> getGraph() throws IOException {
        List<List<Integer>> graph = new ArrayList<>();

        for (String line_file_name : LINES_FILES_NAMES) {
            addLineToGraph(graph, line_file_name);
        }

        return graph;
    }

    private static void addLineToGraph(List<List<Integer>> graph, String line_file_name) throws IOException {
        Path filePath = Paths.get(LINES_FILES_PARENT_FOLDER_PATH + line_file_name);
        List<String> stations = Files.readAllLines(filePath);

        for (int i = 0; i < stations.size() - 1; i++) {
            addTwoStationsInBothDirections(graph, STATIONS_NAMES_TO_INTS.get(stations.get(i)), STATIONS_NAMES_TO_INTS.get(stations.get(i + 1)));
        }
    }

    private static void addTwoStationsInBothDirections(List<List<Integer>> graph, Integer station1, Integer station2) {
        List<Integer> edgeInFirstDirection = Arrays.asList(
                station1,
                station2
        );
        List<Integer> edgeInSecondDirection = Arrays.asList(
                station2,
                station1
        );

        graph.add(edgeInFirstDirection);
        graph.add(edgeInSecondDirection);
    }
}
