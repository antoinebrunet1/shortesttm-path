import { ChangeDetectionStrategy, Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-starting-line',
  imports: [],
  templateUrl: './starting-line.html',
  styleUrl: './starting-line.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class StartingLine {
  @Input() allLines: string[] = [];
  @Output() line = new EventEmitter();
}
