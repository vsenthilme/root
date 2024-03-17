import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StockreceiptLineComponent } from './stockreceipt-line.component';

describe('StockreceiptLineComponent', () => {
  let component: StockreceiptLineComponent;
  let fixture: ComponentFixture<StockreceiptLineComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StockreceiptLineComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StockreceiptLineComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
