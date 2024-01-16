import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProposedComponent } from './proposed.component';

describe('ProposedComponent', () => {
  let component: ProposedComponent;
  let fixture: ComponentFixture<ProposedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProposedComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProposedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
