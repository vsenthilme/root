import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExpirationNewComponent } from './expiration-new.component';

describe('ExpirationNewComponent', () => {
  let component: ExpirationNewComponent;
  let fixture: ComponentFixture<ExpirationNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ExpirationNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ExpirationNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
