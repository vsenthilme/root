import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CompanyIdComponent } from './company-id.component';

describe('CompanyIdComponent', () => {
  let component: CompanyIdComponent;
  let fixture: ComponentFixture<CompanyIdComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CompanyIdComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CompanyIdComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
