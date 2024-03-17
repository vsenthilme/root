import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TrsanctionreportscreenComponent } from './trsanctionreportscreen.component';

describe('TrsanctionreportscreenComponent', () => {
  let component: TrsanctionreportscreenComponent;
  let fixture: ComponentFixture<TrsanctionreportscreenComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TrsanctionreportscreenComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TrsanctionreportscreenComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
