import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreatepartnerComponent } from './createpartner.component';

describe('CreatepartnerComponent', () => {
  let component: CreatepartnerComponent;
  let fixture: ComponentFixture<CreatepartnerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreatepartnerComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CreatepartnerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
