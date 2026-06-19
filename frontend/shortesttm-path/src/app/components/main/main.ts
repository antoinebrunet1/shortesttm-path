import { Component } from '@angular/core';
import { Instructions } from '../instructions/instructions';
import { LinesService } from '../../services/lines-service/lines-service';
import { AsyncPipe } from '@angular/common';
import { StationsService } from '../../services/stations-service/stations-service';
import { StartStation } from '../start-station/start-station';

@Component({
  selector: 'app-main',
  imports: [Instructions, AsyncPipe, StartStation],
  templateUrl: './main.html',
  styleUrl: './main.css',
})
export class Main {
  allLines$: any;

  constructor(private stationsService: StationsService) {}

  ngOnInit() {
    this.allLines$ = this.stationsService.getAllStations();
  }
}
