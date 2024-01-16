import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientcatEditComponent } from './clientcat-edit.component';

describe('ClientcatEditComponent', () => {
  let component: ClientcatEditComponent;
  let fixture: ComponentFixture<ClientcatEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ClientcatEditComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ClientcatEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
