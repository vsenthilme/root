import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientCashReciptComponent } from './client-cash-recipt.component';

describe('ClientCashReciptComponent', () => {
  let component: ClientCashReciptComponent;
  let fixture: ComponentFixture<ClientCashReciptComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ClientCashReciptComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ClientCashReciptComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
