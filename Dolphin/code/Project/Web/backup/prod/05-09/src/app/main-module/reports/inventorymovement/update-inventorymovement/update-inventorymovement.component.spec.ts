import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateInventorymovementComponent } from './update-inventorymovement.component';

describe('UpdateInventorymovementComponent', () => {
  let component: UpdateInventorymovementComponent;
  let fixture: ComponentFixture<UpdateInventorymovementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UpdateInventorymovementComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UpdateInventorymovementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
