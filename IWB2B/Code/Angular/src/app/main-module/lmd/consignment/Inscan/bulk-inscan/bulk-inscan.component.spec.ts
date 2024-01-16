import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BulkInscanComponent } from './bulk-inscan.component';

describe('BulkInscanComponent', () => {
  let component: BulkInscanComponent;
  let fixture: ComponentFixture<BulkInscanComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BulkInscanComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BulkInscanComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
