import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CaseCategoryComponent } from './case-category.component';

describe('CaseCategoryComponent', () => {
  let component: CaseCategoryComponent;
  let fixture: ComponentFixture<CaseCategoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CaseCategoryComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CaseCategoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
