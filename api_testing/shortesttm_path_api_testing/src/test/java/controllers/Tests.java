package controllers;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class Tests {
  private final String STATIONS_CONTROLLER_PATH = "/stations";
  private final String SHORTEST_PATH_CONTROLLER_PATH = "/shortest_path";
  private final String LINES_CONTROLLER_PATH = "/lines";

  @BeforeClass
  public void setup() {
    RestAssured.baseURI = "http://localhost:8080";
  }

  private void validateBody(Response response, String testCaseName) throws IOException {
    String bodyAsString = response.getBody().asString();
    String expectedBodyAsString = Files.readString(Path.of(
            "src/test/resources/expected_bodies/" + testCaseName + ".json"),
        StandardCharsets.UTF_8);
    JsonElement body = JsonParser.parseString(bodyAsString);
    JsonElement expectedBody = JsonParser.parseString(expectedBodyAsString);

    Assert.assertEquals(body, expectedBody);
  }

  private void getShortestPathHappyPath(String startingStation, String destinationStation,
                                        String testCaseName) throws IOException {
    Response response = getResponseForShortestPath(startingStation, destinationStation);
    int statusCode = response.getStatusCode();

    Assert.assertEquals(statusCode, 200);
    validateBody(response, testCaseName);
  }

  private Response getResponseForShortestPath(String startingStation, String destinationStation) {
    return RestAssured.
        given().
        queryParam("startingStation", startingStation).
        queryParam("destinationStation", destinationStation)
        .get(SHORTEST_PATH_CONTROLLER_PATH);
  }

  private void getAllStationsLineHappyPath(String line, String testCaseName) throws IOException {
    Response response = RestAssured.get(LINES_CONTROLLER_PATH + "/stations/" + line);
    int statusCode = response.getStatusCode();

    Assert.assertEquals(statusCode, 200);
    validateBody(response, testCaseName);
  }

  @Test
  public void getAllStationsAlphaOrderHappyPath() throws IOException {
    Response response = RestAssured.get(STATIONS_CONTROLLER_PATH + "/alphabetical-order");
    int statusCode = response.getStatusCode();

    Assert.assertEquals(statusCode, 200);
    validateBody(response, "getAllStationsAlphaOrderHappyPath");
  }

  @Test
  public void getShortestPathOneTransferHappyPath() throws IOException {
    getShortestPathHappyPath("Laurier", "Charlevoix",
        "getShortestPathOneTransferHappyPath");
  }

  @Test
  public void getShortestPathTwoTransfersHappyPath() throws IOException {
    getShortestPathHappyPath("Acadie", "Angrignon",
        "getShortestPathTwoTransfersHappyPath");
  }

  @Test
  public void getShortestPathSameLine() {
    Response response = getResponseForShortestPath("Radisson",
        "Atwater");
    int statusCode = response.getStatusCode();

    Assert.assertEquals(statusCode, 400);

    String bodyAsString = response.getBody().asString();
    String expectedBodyAsString = "Provided stations are on the same line";

    Assert.assertEquals(bodyAsString, expectedBodyAsString);
  }

  @Test
  public void getAllLinesHappyPath() throws IOException {
    Response response = RestAssured.get(LINES_CONTROLLER_PATH);
    int statusCode = response.getStatusCode();

    Assert.assertEquals(statusCode, 200);
    validateBody(response, "getAllLinesHappyPath");
  }

  @Test
  public void getAllStationsBlueLineHappyPath() throws IOException {
    getAllStationsLineHappyPath("BLUE", "getAllStationsBlueLineHappyPath");
  }

  @Test
  public void getAllStationsGreenLineHappyPath() throws IOException {
    getAllStationsLineHappyPath("GREEN", "getAllStationsGreenLineHappyPath");
  }
}
