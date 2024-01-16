import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NumberrangecodeNewComponent } from './numberrangecode-new.component';

describe('NumberrangecodeNewComponent', () => {
  let component: NumberrangecodeNewComponent;
  let fixture: ComponentFixture<NumberrangecodeNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NumberrangecodeNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NumberrangecodeNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
