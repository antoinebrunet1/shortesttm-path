import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { StartingLine } from '../starting-line/starting-line';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AsyncPipe } from '@angular/common';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-starting-line-station',
  imports: [StartingLine, AsyncPipe],
  templateUrl: './starting-line-station.html',
  styleUrl: './starting-line-station.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class StartingLineStation {
  @Input() allLines: any;
}
