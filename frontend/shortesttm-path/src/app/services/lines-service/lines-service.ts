import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class LinesService {
  private http = inject(HttpClient);

  getAllLines(): any {
    const path = 'http://localhost:8080/lines';
    const headers = new HttpHeaders().set('X-API-KEY', environment.apiKey);
    return this.http.get<string[]>(path, { headers });
  }
}
