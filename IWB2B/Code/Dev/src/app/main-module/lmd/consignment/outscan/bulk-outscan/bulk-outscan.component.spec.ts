import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BulkOutscanComponent } from './bulk-outscan.component';

describe('BulkOutscanComponent', () => {
  let component: BulkOutscanComponent;
  let fixture: ComponentFixture<BulkOutscanComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BulkOutscanComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BulkOutscanComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
