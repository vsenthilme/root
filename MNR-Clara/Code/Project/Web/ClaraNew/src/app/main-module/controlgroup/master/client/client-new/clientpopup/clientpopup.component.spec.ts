import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientpopupComponent } from './clientpopup.component';

describe('ClientpopupComponent', () => {
  let component: ClientpopupComponent;
  let fixture: ComponentFixture<ClientpopupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ClientpopupComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ClientpopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
