import { Component } from '@angular/core';
import { Instructions } from '../instructions/instructions';

@Component({
  selector: 'app-main',
  imports: [Instructions],
  templateUrl: './main.html',
  styleUrl: './main.css',
})
export class Main {}
