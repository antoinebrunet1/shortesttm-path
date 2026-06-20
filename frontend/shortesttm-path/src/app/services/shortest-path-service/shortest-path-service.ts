import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
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
    const headers = new HttpHeaders().set('X-API-KEY', environment.apiKey);

    return this.http.get<ShortestPathInterface>(path, {
      headers,
      params: {
        startingStation: startingStation,
        destinationStation: destinationStation,
      },
    });
  }
}
