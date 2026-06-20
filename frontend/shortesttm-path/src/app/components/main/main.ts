import { Component } from '@angular/core';
import { Instructions } from '../instructions/instructions';
import { LinesService } from '../../services/lines-service/lines-service';
import { AsyncPipe } from '@angular/common';
import { StationsService } from '../../services/stations-service/stations-service';
import { StartStation } from '../start-station/start-station';
import { ShortestPathService } from '../../services/shortest-path-service/shortest-path-service';
import { ShortestPath } from '../shortest-path/shortest-path';

@Component({
  selector: 'app-main',
  imports: [Instructions, AsyncPipe, StartStation, ShortestPath],
  templateUrl: './main.html',
  styleUrl: './main.css',
})
export class Main {
  allLines$: any;
  startingStation: string;
  destinationStation: string;
  shortestPath$: ShortestPathInterface | null;

  constructor(
    private stationsService: StationsService,
    private shortestPathService: ShortestPathService,
  ) {
    this.startingStation = '';
    this.destinationStation = '';
    this.shortestPath$ = null;
  }

  ngOnInit() {
    this.allLines$ = this.stationsService.getAllStations();
  }

  handleStartingStationFromChild(data: string) {
    this.startingStation = data;
    console.log('sData received in parent:', this.startingStation);
  }

  handleDestinationStationFromChild(data: string) {
    this.destinationStation = data;
    console.log('dData received in parent:', this.destinationStation);
  }

  updateShortestPath() {
    this.shortestPathService
      .getShortestPath(this.startingStation, this.destinationStation)
      .subscribe((value) => (this.shortestPath$ = value));
  }
}
