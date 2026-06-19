import { Component } from '@angular/core';
import { Instructions } from '../instructions/instructions';
import { StartingLineStation } from '../starting-line-station/starting-line-station';

@Component({
  selector: 'app-main',
  imports: [Instructions, StartingLineStation],
  templateUrl: './main.html',
  styleUrl: './main.css',
})
export class Main {}
