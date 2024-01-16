import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Ownershipop2Component } from './ownershipop2.component';

describe('Ownershipop2Component', () => {
  let component: Ownershipop2Component;
  let fixture: ComponentFixture<Ownershipop2Component>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ Ownershipop2Component ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(Ownershipop2Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
