import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WarehousematerialComponent } from './warehousematerial.component';

describe('WarehousematerialComponent', () => {
  let component: WarehousematerialComponent;
  let fixture: ComponentFixture<WarehousematerialComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WarehousematerialComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WarehousematerialComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
