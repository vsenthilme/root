import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientAssignmentNewComponent } from './client-assignment-new.component';

describe('ClientAssignmentNewComponent', () => {
  let component: ClientAssignmentNewComponent;
  let fixture: ComponentFixture<ClientAssignmentNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ClientAssignmentNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ClientAssignmentNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
