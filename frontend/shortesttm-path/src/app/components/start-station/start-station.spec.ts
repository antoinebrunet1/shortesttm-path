import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StartStation } from './start-station';

describe('StartStation', () => {
  let component: StartStation;
  let fixture: ComponentFixture<StartStation>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StartStation]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StartStation);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
