import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-instructions',
  imports: [MatButtonModule, MatIconModule],
  templateUrl: './instructions.html',
  styleUrl: './instructions.css',
})
export class Instructions {}
