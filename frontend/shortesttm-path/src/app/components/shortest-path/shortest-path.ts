import { LowerCasePipe } from '@angular/common';
import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import {StationEnum} from '../../enums/StationEnum';

@Component({
  selector: 'app-shortest-path',
  imports: [LowerCasePipe],
  templateUrl: './shortest-path.html',
  styleUrl: './shortest-path.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ShortestPath {
  @Input() shortestPath: ShortestPathInterface | null = null;
  protected readonly StationEnum = StationEnum;
  protected readonly Object = Object;

  getDisplayName(key: string) {
    return Object.values(StationEnum)[Object.keys(StationEnum).indexOf(key)];
  }
}
