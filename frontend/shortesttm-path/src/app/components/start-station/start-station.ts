import { ChangeDetectionStrategy, Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';

@Component({
  selector: 'app-start-station',
  imports: [MatFormFieldModule, MatSelectModule, MatInputModule, FormsModule],
  templateUrl: './start-station.html',
  styleUrl: './start-station.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class StartStation {
  @Input() allStations: any;
  @Output() startingStation = new EventEmitter<string>();

  change(event: any) {
    if (!event.source.selected) {
      return;
    }

    this.startingStation.emit(event.source.value);
  }
}
