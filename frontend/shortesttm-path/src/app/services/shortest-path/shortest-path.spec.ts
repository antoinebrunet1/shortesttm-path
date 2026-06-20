import { TestBed } from '@angular/core/testing';

import { ShortestPath } from './shortest-path';

describe('ShortestPath', () => {
  let service: ShortestPath;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ShortestPath);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
