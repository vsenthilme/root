import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubsManagementComponent } from './subs-management.component';

describe('SubsManagementComponent', () => {
  let component: SubsManagementComponent;
  let fixture: ComponentFixture<SubsManagementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SubsManagementComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SubsManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
