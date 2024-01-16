import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FlRentNewComponent } from './fl-rent-new.component';

describe('FlRentNewComponent', () => {
  let component: FlRentNewComponent;
  let fixture: ComponentFixture<FlRentNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FlRentNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FlRentNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
