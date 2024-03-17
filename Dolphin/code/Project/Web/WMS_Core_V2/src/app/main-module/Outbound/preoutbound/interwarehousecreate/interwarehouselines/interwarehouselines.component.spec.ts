import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InterwarehouselinesComponent } from './interwarehouselines.component';

describe('InterwarehouselinesComponent', () => {
  let component: InterwarehouselinesComponent;
  let fixture: ComponentFixture<InterwarehouselinesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InterwarehouselinesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InterwarehouselinesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
