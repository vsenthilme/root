import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WordOrderStatusComponent } from './word-order-status.component';

describe('WordOrderStatusComponent', () => {
  let component: WordOrderStatusComponent;
  let fixture: ComponentFixture<WordOrderStatusComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WordOrderStatusComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WordOrderStatusComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
