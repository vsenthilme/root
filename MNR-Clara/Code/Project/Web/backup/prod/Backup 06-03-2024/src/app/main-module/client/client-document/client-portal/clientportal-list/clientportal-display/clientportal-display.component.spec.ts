import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientportalDisplayComponent } from './clientportal-display.component';

describe('ClientportalDisplayComponent', () => {
  let component: ClientportalDisplayComponent;
  let fixture: ComponentFixture<ClientportalDisplayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ClientportalDisplayComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ClientportalDisplayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
