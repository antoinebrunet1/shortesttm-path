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

    private static final int NUMBER_OF_VERTICES = 68;

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

    public void printShortestPath(String startStation, String destinationStation) {

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
