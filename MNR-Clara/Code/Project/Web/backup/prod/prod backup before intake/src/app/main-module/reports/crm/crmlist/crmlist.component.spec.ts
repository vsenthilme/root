import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CrmlistComponent } from './crmlist.component';

describe('CrmlistComponent', () => {
  let component: CrmlistComponent;
  let fixture: ComponentFixture<CrmlistComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CrmlistComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CrmlistComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
