import { ChangeDetectionStrategy, Component, Input } from '@angular/core';

@Component({
  selector: 'app-start-station',
  imports: [],
  templateUrl: './start-station.html',
  styleUrl: './start-station.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class StartStation {
  @Input() allStations: any;
}
