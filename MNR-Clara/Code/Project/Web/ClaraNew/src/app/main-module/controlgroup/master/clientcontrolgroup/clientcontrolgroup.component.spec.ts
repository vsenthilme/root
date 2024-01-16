import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientcontrolgroupComponent } from './clientcontrolgroup.component';

describe('ClientcontrolgroupComponent', () => {
  let component: ClientcontrolgroupComponent;
  let fixture: ComponentFixture<ClientcontrolgroupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ClientcontrolgroupComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ClientcontrolgroupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
