import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BulkArrivalComponent } from './bulk-arrival.component';

describe('BulkArrivalComponent', () => {
  let component: BulkArrivalComponent;
  let fixture: ComponentFixture<BulkArrivalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BulkArrivalComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BulkArrivalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
