import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StockreceiptComponent } from './stockreceipt.component';

describe('StockreceiptComponent', () => {
  let component: StockreceiptComponent;
  let fixture: ComponentFixture<StockreceiptComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StockreceiptComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StockreceiptComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
