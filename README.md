<h1 align="center">
    🚆 ShortestTM Path 🚆
</h1>

<p align="center">
  <b>App to find the shortest path between two STM metro stations</b>
</p>

<div align="center">

![GitHub License](https://img.shields.io/github/license/antoinebrunet1/shortesttm-path) ![GitHub last commit](https://img.shields.io/github/last-commit/antoinebrunet1/shortesttm-path) [![Java](https://img.shields.io/badge/Java-%23ED8B00.svg?logo=openjdk&logoColor=white)](#) [![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?logo=springboot&logoColor=fff)](#) [![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?logo=springsecurity&logoColor=fff)](#) [![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?logo=intellij-idea&logoColor=white)](#) [![Postman](https://img.shields.io/badge/Postman-FF6C37?logo=postman&logoColor=white)](#) [![Windows](https://custom-icon-badges.demolab.com/badge/Windows-0078D6?logo=windows11&logoColor=white)](#) [![PowerShell](https://custom-icon-badges.demolab.com/badge/PowerShell-5391FE?logo=powershell-white&logoColor=fff)](#)

</div>

<hr>

## 💻 Local setup 💻

1. Fork the repository.
2. Run the Spring Boot backend API by running the `main` method in

```
backend/shortesttm_path/src/main/java/com/example/shortesttm_path/ShortesttmPathApplication.java
```

##  💡 Algorithm overview 💡

I get the shortest path as a list of stations using an algorithm called "Shortest path in an unweighted graph" I found at https://www.geeksforgeeks.org/dsa/shortest-path-unweighted-graph/.

This algorithm is using integers. I just map the stations names to integers to find the path and after that I map the integers back to stations names.

For the HTTP response, I only want to send, for the path, the starting station, the destination station and stations used to switch lines between the first and last stations.

To get the stations used to switch lines between the first and last stations, I take the stations that have at least two lines and that are used to actually switches lines. That can be detected by making sure that the previous and next stations are not on the same line.