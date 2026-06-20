import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class StationsService {
  private http = inject(HttpClient);

  getAllStationsInAlphabeticalOrder(): any {
    const path = `${environment.baseUrl}/stations/alphabetical-order`;

    return this.http.get<string[]>(path);
  }
}
