import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StatusIdComponent } from './status-id.component';

describe('StatusIdComponent', () => {
  let component: StatusIdComponent;
  let fixture: ComponentFixture<StatusIdComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StatusIdComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StatusIdComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
