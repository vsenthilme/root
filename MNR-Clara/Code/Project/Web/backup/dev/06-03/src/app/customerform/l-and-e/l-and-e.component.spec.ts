import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LAndEComponent } from './l-and-e.component';

describe('LAndEComponent', () => {
  let component: LAndEComponent;
  let fixture: ComponentFixture<LAndEComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LAndEComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LAndEComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
