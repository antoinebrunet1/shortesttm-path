import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShortestPath } from './shortest-path';

describe('ShortestPath', () => {
  let component: ShortestPath;
  let fixture: ComponentFixture<ShortestPath>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShortestPath],
    }).compileComponents();

    fixture = TestBed.createComponent(ShortestPath);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
