import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdditionalInformationForm007Component } from './additional-information-form007.component';

describe('AdditionalInformationForm007Component', () => {
  let component: AdditionalInformationForm007Component;
  let fixture: ComponentFixture<AdditionalInformationForm007Component>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdditionalInformationForm007Component ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdditionalInformationForm007Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
