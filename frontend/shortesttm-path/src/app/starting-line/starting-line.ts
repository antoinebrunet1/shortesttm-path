import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-starting-line',
  imports: [],
  templateUrl: './starting-line.html',
  styleUrl: './starting-line.css',
})
export class StartingLine {
  @Input() allLines: any;
  @Output() line = new EventEmitter();
}
