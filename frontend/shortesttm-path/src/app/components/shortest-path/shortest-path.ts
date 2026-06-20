import { ChangeDetectionStrategy, Component, Input } from '@angular/core';

@Component({
  selector: 'app-shortest-path',
  imports: [],
  templateUrl: './shortest-path.html',
  styleUrl: './shortest-path.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ShortestPath {
  @Input() shortestPath: ShortestPathInterface | null = null;
}
