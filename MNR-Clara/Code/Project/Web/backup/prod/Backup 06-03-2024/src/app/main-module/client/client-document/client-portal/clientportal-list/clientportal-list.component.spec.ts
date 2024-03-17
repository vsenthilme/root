import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientportalListComponent } from './clientportal-list.component';

describe('ClientportalListComponent', () => {
  let component: ClientportalListComponent;
  let fixture: ComponentFixture<ClientportalListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ClientportalListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ClientportalListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
