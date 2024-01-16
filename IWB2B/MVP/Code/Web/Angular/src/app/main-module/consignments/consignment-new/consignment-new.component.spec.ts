import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConsignmentNewComponent } from './consignment-new.component';

describe('ConsignmentNewComponent', () => {
  let component: ConsignmentNewComponent;
  let fixture: ComponentFixture<ConsignmentNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConsignmentNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ConsignmentNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
