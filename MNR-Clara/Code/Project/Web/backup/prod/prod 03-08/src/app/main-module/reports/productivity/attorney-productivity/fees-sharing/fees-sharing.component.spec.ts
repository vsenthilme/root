import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FeesSharingComponent } from './fees-sharing.component';

describe('FeesSharingComponent', () => {
  let component: FeesSharingComponent;
  let fixture: ComponentFixture<FeesSharingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FeesSharingComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FeesSharingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
