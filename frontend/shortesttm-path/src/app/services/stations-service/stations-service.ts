import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class StationsService {
  private http = inject(HttpClient);

  getAllStationsInAlphabeticalOrder(): any {
    const path = 'http://localhost:8080/stations/alphabetical-order';

    return this.http.get<string[]>(path);
  }
}
