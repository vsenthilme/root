import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmailNEWComponent } from './email-new.component';

describe('EmailNEWComponent', () => {
  let component: EmailNEWComponent;
  let fixture: ComponentFixture<EmailNEWComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EmailNEWComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EmailNEWComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
