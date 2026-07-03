import { AsyncPipe, NgStyle } from '@angular/common';
import { ChangeDetectorRef, Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { MatError } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { catchError, EMPTY, Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { ShortestPathService } from '../../services/shortest-path-service/shortest-path-service';
import { StationsService } from '../../services/stations-service/stations-service';
import { Instructions } from '../instructions/instructions';
import { ShortestPath } from '../shortest-path/shortest-path';
import { Station } from '../station/station';

@Component({
  selector: 'app-main',
  imports: [
    Instructions,
    AsyncPipe,
    Station,
    ShortestPath,
    MatButtonModule,
    MatError,
    MatIconModule,
    MatCardModule,
    MatDividerModule,
    NgStyle,
  ],
  templateUrl: './main.html',
  styleUrl: './main.css',
})
export class Main {
  allLines$: Observable<string[]>;
  startingStation: string;
  destinationStation: string;
  shortestPath$: Observable<ShortestPathInterface> = EMPTY;
  gotOnSameLineError: boolean = false;
  pathReturned: boolean = false;

  constructor(
    private stationsService: StationsService,
    private shortestPathService: ShortestPathService,
    private ref: ChangeDetectorRef,
  ) {
    this.startingStation = 'Acadie';
    this.destinationStation = 'Acadie';
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
          this.pathReturned = false;

          if (err.status === 400) {
            this.gotOnSameLineError = true;
          } else {
            this.gotOnSameLineError = false;
          }

          this.ref.detectChanges();

          return EMPTY;
        }),
        tap((data) => {
          this.pathReturned = true;
          this.gotOnSameLineError = false;

          this.ref.detectChanges();
        }),
      );
  }
}
