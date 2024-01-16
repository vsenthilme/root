import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BillNewComponent } from './bill-new.component';

describe('BillNewComponent', () => {
  let component: BillNewComponent;
  let fixture: ComponentFixture<BillNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BillNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BillNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
