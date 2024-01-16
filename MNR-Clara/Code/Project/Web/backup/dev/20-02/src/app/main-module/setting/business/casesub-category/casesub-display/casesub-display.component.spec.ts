import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CasesubDisplayComponent } from './casesub-display.component';

describe('CasesubDisplayComponent', () => {
  let component: CasesubDisplayComponent;
  let fixture: ComponentFixture<CasesubDisplayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CasesubDisplayComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CasesubDisplayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
