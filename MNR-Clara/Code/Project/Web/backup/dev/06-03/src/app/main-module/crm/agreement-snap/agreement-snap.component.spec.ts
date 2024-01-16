import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AgreementSnapComponent } from './agreement-snap.component';

describe('AgreementSnapComponent', () => {
  let component: AgreementSnapComponent;
  let fixture: ComponentFixture<AgreementSnapComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AgreementSnapComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AgreementSnapComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
