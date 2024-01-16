import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AssignPartnerComponent } from './assign-partner.component';

describe('AssignPartnerComponent', () => {
  let component: AssignPartnerComponent;
  let fixture: ComponentFixture<AssignPartnerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AssignPartnerComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AssignPartnerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
