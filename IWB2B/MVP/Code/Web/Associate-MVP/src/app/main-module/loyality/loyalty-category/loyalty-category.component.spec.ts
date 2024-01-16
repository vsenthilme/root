import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoyaltyCategoryComponent } from './loyalty-category.component';

describe('LoyaltyCategoryComponent', () => {
  let component: LoyaltyCategoryComponent;
  let fixture: ComponentFixture<LoyaltyCategoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LoyaltyCategoryComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LoyaltyCategoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
