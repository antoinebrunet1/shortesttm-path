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

  @BeforeClass
  public void setup() {
    RestAssured.baseURI = "http://localhost:8080";
  }

  @org.testng.annotations.Test
  public void getAllStationsAlphaOrderHappyPath() throws IOException {
    String endpoint = STATIONS_CONTROLLER_PATH + "/alphabetical-order";
    Response response = RestAssured.get(endpoint);
    int statusCode = response.getStatusCode();

    Assert.assertEquals(statusCode, 200);

    String bodyAsString = response.getBody().asString();
    String expectedBodyAsString = Files.readString(Path.of(
        "src/test/resources/all_stations_alpha_order.json"), StandardCharsets.UTF_8);
    JsonElement body = JsonParser.parseString(bodyAsString);
    JsonElement expectedBody = JsonParser.parseString(expectedBodyAsString);

    Assert.assertEquals(body, expectedBody);
  }

  @Test
  public void getShortestPathOneTransferHappyPath() throws IOException {
    String startingStation = "Laurier";
    String destinationStation = "Charlevoix";
    Response response = RestAssured.
        given().
        queryParam("startingStation", startingStation).
        queryParam("destinationStation", destinationStation)
        .get(SHORTEST_PATH_CONTROLLER_PATH);
    int statusCode = response.getStatusCode();
    Assert.assertEquals(statusCode, 200);
    String bodyAsString = response.getBody().asString();
    String expectedBodyAsString = Files.readString(Path.of(
        "src/test/resources/expected_bodies/getShortestPathOneTransferHappyPath.json"),
        StandardCharsets.UTF_8);
    JsonElement body = JsonParser.parseString(bodyAsString);
    JsonElement expectedBody = JsonParser.parseString(expectedBodyAsString);
    Assert.assertEquals(body, expectedBody);
  }
}
