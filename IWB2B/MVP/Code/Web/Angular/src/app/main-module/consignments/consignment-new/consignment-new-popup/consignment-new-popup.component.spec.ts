import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConsignmentNewPopupComponent } from './consignment-new-popup.component';

describe('ConsignmentNewPopupComponent', () => {
  let component: ConsignmentNewPopupComponent;
  let fixture: ComponentFixture<ConsignmentNewPopupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConsignmentNewPopupComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ConsignmentNewPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
