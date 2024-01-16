import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientcontrolgroupNewComponent } from './clientcontrolgroup-new.component';

describe('ClientcontrolgroupNewComponent', () => {
  let component: ClientcontrolgroupNewComponent;
  let fixture: ComponentFixture<ClientcontrolgroupNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ClientcontrolgroupNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ClientcontrolgroupNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
