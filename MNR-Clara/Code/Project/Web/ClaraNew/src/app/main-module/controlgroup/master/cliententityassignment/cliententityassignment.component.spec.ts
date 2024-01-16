import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CliententityassignmentComponent } from './cliententityassignment.component';

describe('CliententityassignmentComponent', () => {
  let component: CliententityassignmentComponent;
  let fixture: ComponentFixture<CliententityassignmentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CliententityassignmentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CliententityassignmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
