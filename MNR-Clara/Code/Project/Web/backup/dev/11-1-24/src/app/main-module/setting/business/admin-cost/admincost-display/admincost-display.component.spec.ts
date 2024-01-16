import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdmincostDisplayComponent } from './admincost-display.component';

describe('AdmincostDisplayComponent', () => {
  let component: AdmincostDisplayComponent;
  let fixture: ComponentFixture<AdmincostDisplayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdmincostDisplayComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdmincostDisplayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
