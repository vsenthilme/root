import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CasecategoryDisplayComponent } from './casecategory-display.component';

describe('CasecategoryDisplayComponent', () => {
  let component: CasecategoryDisplayComponent;
  let fixture: ComponentFixture<CasecategoryDisplayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CasecategoryDisplayComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CasecategoryDisplayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
