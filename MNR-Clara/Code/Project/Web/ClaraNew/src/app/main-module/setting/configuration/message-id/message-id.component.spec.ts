import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MessageIdComponent } from './message-id.component';

describe('MessageIdComponent', () => {
  let component: MessageIdComponent;
  let fixture: ComponentFixture<MessageIdComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MessageIdComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MessageIdComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
