import { ChangeDetectionStrategy, Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';

@Component({
  selector: 'app-start-station',
  imports: [MatFormFieldModule, MatSelectModule, MatInputModule, FormsModule],
  templateUrl: './station.html',
  styleUrl: './station.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Station {
  @Input() allStations: any;
  @Output() station = new EventEmitter<string>();

  change(event: any) {
    if (!event.source.selected) {
      return;
    }

    this.station.emit(event.source.value);
  }
}
