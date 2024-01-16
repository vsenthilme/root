import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MarkNprComponent } from './mark-npr.component';

describe('MarkNprComponent', () => {
  let component: MarkNprComponent;
  let fixture: ComponentFixture<MarkNprComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MarkNprComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MarkNprComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
