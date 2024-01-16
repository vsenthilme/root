import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CopyfromComponent } from './copyfrom.component';

describe('CopyfromComponent', () => {
  let component: CopyfromComponent;
  let fixture: ComponentFixture<CopyfromComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CopyfromComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CopyfromComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
