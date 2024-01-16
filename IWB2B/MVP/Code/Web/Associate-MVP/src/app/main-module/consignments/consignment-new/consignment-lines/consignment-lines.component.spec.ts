import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConsignmentLinesComponent } from './consignment-lines.component';

describe('ConsignmentLinesComponent', () => {
  let component: ConsignmentLinesComponent;
  let fixture: ComponentFixture<ConsignmentLinesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConsignmentLinesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ConsignmentLinesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
