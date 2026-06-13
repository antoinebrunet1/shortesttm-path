<h1 align="center">
    🚆 ShortestTM Path 🚆
</h1>

<p align="center">
  <b>App to find the shortest path between two STM metro stations</b>
</p>

<div align="center">

[![Java](https://img.shields.io/badge/Java-%23ED8B00.svg?logo=openjdk&logoColor=white)](#) ![Maven](https://img.shields.io/badge/apachemaven-C71A36.svg?logo=apachemaven&logoColor=white) [![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?logo=springboot&logoColor=fff)](#) [![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?logo=springsecurity&logoColor=fff)](#) [![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-2088FF?logo=github-actions&logoColor=white)](#) [![GitHub Pages](https://img.shields.io/badge/GitHub%20Pages-121013?logo=github&logoColor=white)](#)

</div>

<hr>

## 📚 Javadoc documentation 📚

The latest valid (no warnings and errors during the generation) Javadoc is available at [https://antoinebrunet1.github.io/shortesttm-path/](https://antoinebrunet1.github.io/shortesttm-path/).

Using GitHub actions, in `.github/workflows/deploy_javadoc.yml`, on each push on the `main` branch and on each pull request to the `main` branch, I generate the Javadoc and push the folder with the Javadoc to the `gh-pages` branch as the root folder. The Javadoc is deployed using GitHub Actions from the root folder of the `gh-pages` branch.

## 💻 Local setup 💻

1. Fork the repository.
2. Run the Spring Boot backend API by running the `main` method in

    ```
    backend/shortesttm_path/src/main/java/com/example/shortesttm_path/ShortesttmPathApplication.java
    ```
3. Call the backend API using this URL:

    ```
    http://localhost:8080/shortest_path?startingStation=startingStation&destinationStation=destinationStation
    ```
   
    with values for the two stations. The stations names can be found in the `.txt` files under this folder:

   ```
   backend/shortesttm_path/src/main/resources/static
   ```
   
   The two stations should not be on the same line.

##  💡 Algorithm overview 💡

I get the shortest path as a list of stations using an algorithm called "Shortest path in an unweighted graph" I found at https://www.geeksforgeeks.org/dsa/shortest-path-unweighted-graph/.

This algorithm is using integers. I just map the stations names to integers to find the path and after that I map the integers back to stations names.

For the HTTP response, I only want to send, for the path, the starting station, the destination station and stations used to switch lines between the first and last stations.

To get the stations used to switch lines between the first and last stations, I take the stations that have at least two lines and that are used to actually switches lines. That can be detected by making sure that the previous and next stations are not on the same line.

## ✨ Code quality guarantied ✨

The main branch of this repository contains a GitHub Actions CI/CD pipeline to indicate if the code meets the below quality checks or not.

1. The Javadoc documentation is valid.
2. The unit tests coverage is at least 80%. It is by class and by line.

## ✅ Check code quality locally ✅

To check Javadoc and unit tests coverage, run the command

```
mvn clean verify -DskipTests
```

### 📚 Javadoc 📚

To check if the documentation is valid and generate it if it is, run the command

```
mvn clean javadoc:javadoc
```

If you want to run the command for only a particular class, run the command

```
mvn clean javadoc:javadoc -DclassName=
```

and add the name of the class at the end with no spaces before.

The Javadoc is generated in

```
backend/shortesttm_path/target/reports/apidocs
```

You can view it by opening `index.html`.

### 🧪 Unit tests 🧪

In IntelliJ, the unit tests can be run as a whole or individually by clicking a green triangle.

### 🎯 Unit tests coverage 🎯

To check if the coverage is at least 80% (calculated by class and by line), run the command

```
mvn clean verify -DskipTests -Dmaven.javadoc.skip=true
```