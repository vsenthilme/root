import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminCostComponent } from './admin-cost.component';

describe('AdminCostComponent', () => {
  let component: AdminCostComponent;
  let fixture: ComponentFixture<AdminCostComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminCostComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminCostComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
