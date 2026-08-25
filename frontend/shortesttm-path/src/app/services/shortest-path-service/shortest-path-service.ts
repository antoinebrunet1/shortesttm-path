import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {Station} from '../../enums/Station';

@Injectable({
  providedIn: 'root',
})
export class ShortestPathService {
  private http = inject(HttpClient);

  getShortestPath(
    startingStation: keyof typeof Station,
    destinationStation: keyof typeof Station,
  ): Observable<ShortestPathInterface> {
    const path = `${environment.baseUrl}/shortest_path`;

    return this.http.get<ShortestPathInterface>(path, {
      params: {
        startingStation: Station[startingStation],
        destinationStation: Station[destinationStation],
      },
    });
  }
}
