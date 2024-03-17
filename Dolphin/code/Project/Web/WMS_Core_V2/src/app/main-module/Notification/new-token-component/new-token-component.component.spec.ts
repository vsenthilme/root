import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewTokenComponentComponent } from './new-token-component.component';

describe('NewTokenComponentComponent', () => {
  let component: NewTokenComponentComponent;
  let fixture: ComponentFixture<NewTokenComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NewTokenComponentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NewTokenComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
