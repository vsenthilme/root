import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TransferactivityComponent } from './transferactivity.component';

describe('TransferactivityComponent', () => {
  let component: TransferactivityComponent;
  let fixture: ComponentFixture<TransferactivityComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TransferactivityComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TransferactivityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
