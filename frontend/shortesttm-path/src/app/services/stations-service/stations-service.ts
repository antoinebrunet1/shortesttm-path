import { Injectable } from '@angular/core';
import {Observable, of} from 'rxjs';
import {StationEnum} from '../../enums/StationEnum';

@Injectable({
  providedIn: 'root',
})
export class StationsService {
  getAllStationsInAlphabeticalOrder(): Observable<string[]> {
    return of(Object.values(StationEnum));
  }
}
