import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PrebillAaproveComponent } from './prebill-aaprove.component';

describe('PrebillAaproveComponent', () => {
  let component: PrebillAaproveComponent;
  let fixture: ComponentFixture<PrebillAaproveComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PrebillAaproveComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PrebillAaproveComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
