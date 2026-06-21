import { TestBed } from '@angular/core/testing';

import { ShortestPathService } from './shortest-path-service';

import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';

fdescribe('ShortestPathService', () => {
  let service: ShortestPathService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(ShortestPathService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
