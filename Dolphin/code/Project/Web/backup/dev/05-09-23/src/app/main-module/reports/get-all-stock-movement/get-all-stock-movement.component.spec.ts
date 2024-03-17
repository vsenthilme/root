import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GetAllStockMovementComponent } from './get-all-stock-movement.component';

describe('GetAllStockMovementComponent', () => {
  let component: GetAllStockMovementComponent;
  let fixture: ComponentFixture<GetAllStockMovementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GetAllStockMovementComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GetAllStockMovementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
