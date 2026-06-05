package com.example.shortesttm_path.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    public List<List<String>> getEdges() throws IOException {
        List<List<String>> edges = new ArrayList<>();

        for (String line_file_name : LINES_FILES_NAMES) {
            addLineToEdges(edges, line_file_name);
        }

        return edges;
    }

    private void addLineToEdges(List<List<String>> edges, String line_file_name) throws IOException {
        Path filePath = Paths.get(LINES_FILES_PARENT_FOLDER_PATH + line_file_name);
        List<String> stations = Files.readAllLines(filePath);

        for (int i = 0; i < stations.size() - 1; i++) {
            addTwoStationsInBothDirections(edges, stations.get(i), stations.get(i + 1));
        }
    }

    private void addTwoStationsInBothDirections(List<List<String>> edges, String station1, String station2) {
        List<String> edgeInFirstDirection = Arrays.asList(
                station1,
                station2
        );
        List<String> edgeInSecondDirection = Arrays.asList(
                station2,
                station1
        );

        edges.add(edgeInFirstDirection);
        edges.add(edgeInSecondDirection);
    }
}
