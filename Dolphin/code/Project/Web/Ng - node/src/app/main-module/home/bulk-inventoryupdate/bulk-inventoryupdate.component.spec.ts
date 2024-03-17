import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BulkInventoryupdateComponent } from './bulk-inventoryupdate.component';

describe('BulkInventoryupdateComponent', () => {
  let component: BulkInventoryupdateComponent;
  let fixture: ComponentFixture<BulkInventoryupdateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BulkInventoryupdateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BulkInventoryupdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
