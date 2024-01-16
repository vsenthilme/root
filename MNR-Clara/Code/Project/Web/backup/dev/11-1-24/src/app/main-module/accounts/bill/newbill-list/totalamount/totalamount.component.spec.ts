import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TotalamountComponent } from './totalamount.component';

describe('TotalamountComponent', () => {
  let component: TotalamountComponent;
  let fixture: ComponentFixture<TotalamountComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TotalamountComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TotalamountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
