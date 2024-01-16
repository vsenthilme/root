import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CliententityassignmentNewComponent } from './cliententityassignment-new.component';

describe('CliententityassignmentNewComponent', () => {
  let component: CliententityassignmentNewComponent;
  let fixture: ComponentFixture<CliententityassignmentNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CliententityassignmentNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CliententityassignmentNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
