import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PrebillAssignpartnerComponent } from './prebill-assignpartner.component';

describe('PrebillAssignpartnerComponent', () => {
  let component: PrebillAssignpartnerComponent;
  let fixture: ComponentFixture<PrebillAssignpartnerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PrebillAssignpartnerComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PrebillAssignpartnerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
