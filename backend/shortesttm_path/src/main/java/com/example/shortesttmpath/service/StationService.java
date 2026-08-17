package com.example.shortesttmpath.service;

import com.example.shortesttmpath.repository.StationRepository;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * For StationController.
 */
@Service
public class StationService {
  private final StationRepository stationRepository;

  /**
   * The constructor.
   *
   * @param stationRepository The StationRepository.
   */
  public StationService(StationRepository stationRepository) {
    this.stationRepository = stationRepository;
  }

  /**
   * Returns all the stations in alphabetical order.
   *
   * @return All the stations in alphabetical order.
   */
  public List<String> getAllStationsInAlphabeticalOrder() {
    return stationRepository.getAllStationsAlphabeticalOrder();
  }
}
