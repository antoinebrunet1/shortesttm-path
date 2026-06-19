import { Component } from '@angular/core';
import { StartingLine } from '../starting-line/starting-line';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AsyncPipe } from '@angular/common';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-starting-line-station',
  imports: [StartingLine, AsyncPipe],
  templateUrl: './starting-line-station.html',
  styleUrl: './starting-line-station.css',
})
export class StartingLineStation {
  allLines$: any;

  constructor(http: HttpClient) {
    const path = 'http://localhost:8080/lines';

    const headers = new HttpHeaders().set('X-API-KEY', environment.apiKey);
    this.allLines$ = http.get<string[]>(path, { headers });
  }
}
