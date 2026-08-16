package com.example.shortesttmpath.service;

import com.example.shortesttmpath.repository.StationRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StationService {
  @Autowired
  private StationRepository stationRepository;

  /**
   * Returns all the stations in alphabetical order.
   *
   * @return All the stations in alphabetical order.
   */
  public List<String> getAllStationsInAlphabeticalOrder() {
    return stationRepository.getAllStationsAlphabeticalOrder();
  }
}
