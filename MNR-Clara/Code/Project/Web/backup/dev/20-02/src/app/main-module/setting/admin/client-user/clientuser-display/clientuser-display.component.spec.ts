import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientuserDisplayComponent } from './clientuser-display.component';

describe('ClientuserDisplayComponent', () => {
  let component: ClientuserDisplayComponent;
  let fixture: ComponentFixture<ClientuserDisplayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ClientuserDisplayComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ClientuserDisplayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
