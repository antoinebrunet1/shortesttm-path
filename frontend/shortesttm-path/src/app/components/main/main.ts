import { ChangeDetectorRef, Component } from '@angular/core';
import { Instructions } from '../instructions/instructions';
import { AsyncPipe } from '@angular/common';
import { StationsService } from '../../services/stations-service/stations-service';
import { Station } from '../station/station';
import { ShortestPathService } from '../../services/shortest-path-service/shortest-path-service';
import { ShortestPath } from '../shortest-path/shortest-path';
import { MatButtonModule } from '@angular/material/button';
import { catchError, EMPTY } from 'rxjs';
import { EMPTY_OBSERVER } from 'rxjs/internal/Subscriber';

@Component({
  selector: 'app-main',
  imports: [Instructions, AsyncPipe, Station, ShortestPath, MatButtonModule],
  templateUrl: './main.html',
  styleUrl: './main.css',
})
export class Main {
  allLines$: any;
  startingStation: string;
  destinationStation: string;
  shortestPath$: any;
  errorMessage: string | null = null;

  constructor(
    private stationsService: StationsService,
    private shortestPathService: ShortestPathService,
    private ref: ChangeDetectorRef,
  ) {
    this.startingStation = '';
    this.destinationStation = '';
  }

  ngOnInit() {
    this.allLines$ = this.stationsService.getAllStationsInAlphabeticalOrder();
  }

  handleStartingStationFromChild(data: string) {
    this.startingStation = data;
  }

  handleDestinationStationFromChild(data: string) {
    this.destinationStation = data;
  }

  updateShortestPath() {
    this.shortestPath$ = this.shortestPathService
      .getShortestPath(this.startingStation, this.destinationStation)
      .pipe(
        catchError((err) => {
          if (err.status === 400 && err.error === 'Provided stations are on the same line') {
            this.errorMessage = 'Provided stations are on the same line';
          } else {
            this.errorMessage = null;
          }

          this.ref.detectChanges();

          return EMPTY;
        }),
      );

    this.errorMessage = null;
    this.ref.detectChanges();
  }
}
