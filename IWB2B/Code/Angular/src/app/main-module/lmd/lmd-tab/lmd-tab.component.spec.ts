import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LmdTabComponent } from './lmd-tab.component';

describe('LmdTabComponent', () => {
  let component: LmdTabComponent;
  let fixture: ComponentFixture<LmdTabComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LmdTabComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LmdTabComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
