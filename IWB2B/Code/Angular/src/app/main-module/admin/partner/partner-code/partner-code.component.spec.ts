import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PartnerCodeComponent } from './partner-code.component';

describe('PartnerCodeComponent', () => {
  let component: PartnerCodeComponent;
  let fixture: ComponentFixture<PartnerCodeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PartnerCodeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PartnerCodeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
