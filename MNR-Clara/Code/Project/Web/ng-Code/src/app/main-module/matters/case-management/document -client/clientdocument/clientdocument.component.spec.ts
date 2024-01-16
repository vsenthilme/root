import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientdocumentComponent } from './clientdocument.component';

describe('ClientdocumentComponent', () => {
  let component: ClientdocumentComponent;
  let fixture: ComponentFixture<ClientdocumentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ClientdocumentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ClientdocumentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
