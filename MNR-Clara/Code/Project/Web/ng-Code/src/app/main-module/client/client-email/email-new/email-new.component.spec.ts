import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmailNewComponent } from './email-new.component';

describe('EmailNewComponent', () => {
  let component: EmailNewComponent;
  let fixture: ComponentFixture<EmailNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EmailNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EmailNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
