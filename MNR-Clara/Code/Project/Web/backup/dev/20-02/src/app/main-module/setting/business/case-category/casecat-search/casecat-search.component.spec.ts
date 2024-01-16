import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CasecatSearchComponent } from './casecat-search.component';

describe('CasecatSearchComponent', () => {
  let component: CasecatSearchComponent;
  let fixture: ComponentFixture<CasecatSearchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CasecatSearchComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CasecatSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
