import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ShortestPathService {
  private http = inject(HttpClient);

  getShortestPath(
    startingStation: string,
    destinationStation: string,
  ): Observable<ShortestPathInterface> {
    const path = 'http://localhost:8080/shortest_path';

    return this.http.get<ShortestPathInterface>(path, {
      params: {
        startingStation: startingStation,
        destinationStation: destinationStation,
      },
    });
  }
}
