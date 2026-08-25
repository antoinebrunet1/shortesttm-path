import { Injectable } from '@angular/core';
import {Observable, of} from 'rxjs';
import {Station} from '../../enums/Station';

@Injectable({
  providedIn: 'root',
})
export class StationsService {
  getAllStationsInAlphabeticalOrder(): Observable<string[]> {
    return of(Object.values(Station));
  }
}
