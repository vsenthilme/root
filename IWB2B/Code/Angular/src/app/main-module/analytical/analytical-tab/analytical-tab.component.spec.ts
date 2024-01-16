import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AnalyticalTabComponent } from './analytical-tab.component';

describe('AnalyticalTabComponent', () => {
  let component: AnalyticalTabComponent;
  let fixture: ComponentFixture<AnalyticalTabComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AnalyticalTabComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AnalyticalTabComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
