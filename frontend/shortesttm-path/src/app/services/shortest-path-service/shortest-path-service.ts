import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {StationEnum} from '../../enums/StationEnum';

@Injectable({
  providedIn: 'root',
})
export class ShortestPathService {
  private http = inject(HttpClient);

  getShortestPath(
    startingStation: keyof typeof StationEnum,
    destinationStation: keyof typeof StationEnum,
  ): Observable<ShortestPathInterface> {
    const path = `${environment.baseUrl}/shortest_path`;

    return this.http.get<ShortestPathInterface>(path, {
      params: {
        startingStation: StationEnum[startingStation],
        destinationStation: StationEnum[destinationStation],
      },
    });
  }
}
