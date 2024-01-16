import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CrmTabComponent } from './crm-tab.component';

describe('CrmTabComponent', () => {
  let component: CrmTabComponent;
  let fixture: ComponentFixture<CrmTabComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CrmTabComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CrmTabComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
