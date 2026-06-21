import { TestBed } from '@angular/core/testing';

import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { environment } from '../../../environments/environment';
import { StationsService } from './stations-service';

fdescribe('StationsService', () => {
  let service: StationsService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(StationsService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return all stations in alphabetical order', () => {
    // Not all the stations are there, but it is okay for the purposes of this test.
    const expected = ['Acadie', 'Angrignon'];

    service.getAllStationsInAlphabeticalOrder().subscribe((result) => {
      expect(result).toBeTruthy();
      expect(result.length).toEqual(2);
      expect(result[0]).toBeTruthy();
      expect(result[1]).toBeTruthy();
      expect(result[0]).toEqual(expected[0]);
      expect(result[1]).toEqual(expected[1]);
    });

    const path = `${environment.baseUrl}/stations/alphabetical-order`;
    const req = httpMock.expectOne(path);

    expect(req.request.method).toBe('GET');
    req.flush(expected);
  });
});
