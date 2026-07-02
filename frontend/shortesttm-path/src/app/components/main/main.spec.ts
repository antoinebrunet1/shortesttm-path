import { ComponentFixture, TestBed } from '@angular/core/testing';

import { By } from '@angular/platform-browser';
import { of } from 'rxjs';
import { ShortestPathService } from '../../services/shortest-path-service/shortest-path-service';
import { StationsService } from '../../services/stations-service/stations-service';
import { Main } from './main';

fdescribe('Main', () => {
  let component: Main;
  let fixture: ComponentFixture<Main>;

  beforeEach(async () => {
    const stationsServiceSpy = jasmine.createSpyObj<StationsService>([
      'getAllStationsInAlphabeticalOrder',
    ]);
    stationsServiceSpy.getAllStationsInAlphabeticalOrder.and.callFake(function () {
      return of(['Acadie', 'Angrignon']);
    });
    const shortestPathServiceSpy = jasmine.createSpyObj<ShortestPathService>(['getShortestPath']);
    const startingStationReturned = {
      name: 'Acadie',
      line: 'blue',
      direction: 'Snowdon',
    };
    const transfers = [
      {
        name: 'Snowdon',
        line: 'orange',
        direction: 'Montmorency',
      },
      {
        name: 'Lionel-Groulx',
        line: 'green',
        direction: 'Angrignon',
      },
    ];
    shortestPathServiceSpy.getShortestPath.and.callFake(
      function (startingStation, destinationStation) {
        return of({
          startingStation: startingStationReturned,
          destinationStation: 'Angrignon',
          stationsToSwitchLines: transfers,
        });
      },
    );
    await TestBed.configureTestingModule({
      imports: [Main],
      providers: [
        {
          provide: StationsService,
          useValue: stationsServiceSpy,
        },
        {
          provide: ShortestPathService,
          useValue: shortestPathServiceSpy,
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(Main);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('clicking calculate button displays path', () => {
    const calculateButton = fixture.debugElement.query(By.css('button'));
    calculateButton.triggerEventHandler('click');
    fixture.detectChanges();
    const expectedPath =
      ' Start at Acadie and go in the Snowdon direction on the blue line.  At Snowdon, switch to the orange line and go in the Montmorency direction.  At Lionel-Groulx, switch to the green line and go in the Angrignon direction.  Stop at Angrignon. ';
    expect(fixture.debugElement.query(By.css('app-shortest-path')).nativeNode.textContent).toBe(
      expectedPath,
    );
  });
});
