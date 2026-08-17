package com.example.shortesttmpath.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.example.shortesttmpath.repository.StationRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class StationServiceTest {
  @Mock
  StationRepository stationRepository;
  @InjectMocks
  StationService stationService;

  @Test
  public void getAllStationsInAlphabeticalOrderReturnsCorrectStations() {
    List<String> stations = List.of("A", "B", "C");

    when(stationRepository.getAllStationsAlphabeticalOrder()).thenReturn(stations);

    List<String> actualResult = stationService.getAllStationsInAlphabeticalOrder();

    assertEquals(stations, actualResult);
  }
}
