import { Component } from '@angular/core';
import { StartingLine } from '../starting-line/starting-line';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-starting-line-station',
  imports: [StartingLine],
  templateUrl: './starting-line-station.html',
  styleUrl: './starting-line-station.css',
})
export class StartingLineStation {
  allLines: Observable<string[]>;

  constructor(http: HttpClient) {
    const path = 'http://localhost:8080/lines';
    this.allLines = http.get<string[]>(path);
  }
}
