import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StartingLineStation } from './starting-line-station';

describe('StartingLineStation', () => {
  let component: StartingLineStation;
  let fixture: ComponentFixture<StartingLineStation>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StartingLineStation]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StartingLineStation);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
