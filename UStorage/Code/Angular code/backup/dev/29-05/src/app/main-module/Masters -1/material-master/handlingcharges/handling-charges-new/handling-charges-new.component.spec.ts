import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HandlingChargesNewComponent } from './handling-charges-new.component';

describe('HandlingChargesNewComponent', () => {
  let component: HandlingChargesNewComponent;
  let fixture: ComponentFixture<HandlingChargesNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HandlingChargesNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(HandlingChargesNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
