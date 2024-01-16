import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NumberingseriesNewComponent } from './numberingseries-new.component';

describe('NumberingseriesNewComponent', () => {
  let component: NumberingseriesNewComponent;
  let fixture: ComponentFixture<NumberingseriesNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NumberingseriesNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NumberingseriesNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
