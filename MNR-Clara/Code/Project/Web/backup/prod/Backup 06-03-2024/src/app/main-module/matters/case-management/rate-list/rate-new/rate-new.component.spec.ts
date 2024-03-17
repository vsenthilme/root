import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RateNewComponent } from './rate-new.component';

describe('RateNewComponent', () => {
  let component: RateNewComponent;
  let fixture: ComponentFixture<RateNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RateNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RateNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
