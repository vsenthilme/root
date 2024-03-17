import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PartnerNumberingComponent } from './partner-numbering.component';

describe('PartnerNumberingComponent', () => {
  let component: PartnerNumberingComponent;
  let fixture: ComponentFixture<PartnerNumberingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PartnerNumberingComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PartnerNumberingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
