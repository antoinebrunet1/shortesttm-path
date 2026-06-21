import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class StationsService {
  private http = inject(HttpClient);

  getAllStationsInAlphabeticalOrder(): Observable<string[]> {
    const path = `${environment.baseUrl}/stations/alphabetical-order`;

    return this.http.get<string[]>(path);
  }
}
