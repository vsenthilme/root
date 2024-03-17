import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PartnerCountryComponent } from './partner-country.component';

describe('PartnerCountryComponent', () => {
  let component: PartnerCountryComponent;
  let fixture: ComponentFixture<PartnerCountryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PartnerCountryComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PartnerCountryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
