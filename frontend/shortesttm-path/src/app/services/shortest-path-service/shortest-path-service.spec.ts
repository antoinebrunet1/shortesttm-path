import { TestBed } from '@angular/core/testing';

import { ShortestPathService } from './shortest-path-service';

import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { environment } from '../../../environments/environment';

fdescribe('ShortestPathService', () => {
  let service: ShortestPathService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(ShortestPathService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return a valid shortest path', () => {
    const expected = {
      startingStation: 'Acadie',
      destinationStation: 'Angrignon',
      stationsToSwitchLines: ['Snowdon', 'Lionel-Groulx'],
    };

    service
      .getShortestPath(expected.startingStation, expected.destinationStation)
      .subscribe((result) => {
        // Checks that the result object is not null.
        expect(result).toBeTruthy();

        // Checks that the values of the result object are not null.
        expect(result.startingStation).toBeTruthy();
        expect(result.destinationStation).toBeTruthy();
        expect(result.stationsToSwitchLines).toBeTruthy();

        // Checks that the values of the starting and destination stations are correct.
        expect(result.startingStation).toEqual(expected.startingStation);
        expect(result.destinationStation).toEqual(expected.destinationStation);

        // Checks that the stations to switch lines are valid.
        expect(result.stationsToSwitchLines.length).toEqual(2);
        expect(result.stationsToSwitchLines[0]).toEqual(expected.stationsToSwitchLines[0]);
        expect(result.stationsToSwitchLines[1]).toEqual(expected.stationsToSwitchLines[1]);
      });

    const path = `${environment.baseUrl}/shortest_path?startingStation=${expected.startingStation}&destinationStation=${expected.destinationStation}`;
    const req = httpMock.expectOne(path);

    expect(req.request.method).toBe('GET');
    req.flush(expected);
  });
});
