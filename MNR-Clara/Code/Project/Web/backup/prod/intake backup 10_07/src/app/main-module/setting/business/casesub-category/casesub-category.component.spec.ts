import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CasesubCategoryComponent } from './casesub-category.component';

describe('CasesubCategoryComponent', () => {
  let component: CasesubCategoryComponent;
  let fixture: ComponentFixture<CasesubCategoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CasesubCategoryComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CasesubCategoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
