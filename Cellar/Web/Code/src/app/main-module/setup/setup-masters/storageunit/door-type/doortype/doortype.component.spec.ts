import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DoortypeComponent } from './doortype.component';

describe('DoortypeComponent', () => {
  let component: DoortypeComponent;
  let fixture: ComponentFixture<DoortypeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DoortypeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DoortypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
