import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StartingLine } from './starting-line';

describe('StartingLine', () => {
  let component: StartingLine;
  let fixture: ComponentFixture<StartingLine>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StartingLine]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StartingLine);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
