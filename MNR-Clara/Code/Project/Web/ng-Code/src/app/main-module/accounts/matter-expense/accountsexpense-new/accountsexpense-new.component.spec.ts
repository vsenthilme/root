import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccountsexpenseNewComponent } from './accountsexpense-new.component';

describe('AccountsexpenseNewComponent', () => {
  let component: AccountsexpenseNewComponent;
  let fixture: ComponentFixture<AccountsexpenseNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AccountsexpenseNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AccountsexpenseNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
