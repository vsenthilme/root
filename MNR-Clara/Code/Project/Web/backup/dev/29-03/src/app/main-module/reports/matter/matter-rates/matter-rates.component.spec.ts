import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MatterRatesComponent } from './matter-rates.component';

describe('MatterRatesComponent', () => {
  let component: MatterRatesComponent;
  let fixture: ComponentFixture<MatterRatesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MatterRatesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MatterRatesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
