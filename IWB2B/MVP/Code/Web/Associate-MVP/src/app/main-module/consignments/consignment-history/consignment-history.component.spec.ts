import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConsignmentHistoryComponent } from './consignment-history.component';

describe('ConsignmentHistoryComponent', () => {
  let component: ConsignmentHistoryComponent;
  let fixture: ComponentFixture<ConsignmentHistoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConsignmentHistoryComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ConsignmentHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
