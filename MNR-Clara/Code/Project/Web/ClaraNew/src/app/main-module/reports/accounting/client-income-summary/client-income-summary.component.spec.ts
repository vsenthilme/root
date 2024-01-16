import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientIncomeSummaryComponent } from './client-income-summary.component';

describe('ClientIncomeSummaryComponent', () => {
  let component: ClientIncomeSummaryComponent;
  let fixture: ComponentFixture<ClientIncomeSummaryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ClientIncomeSummaryComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ClientIncomeSummaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
