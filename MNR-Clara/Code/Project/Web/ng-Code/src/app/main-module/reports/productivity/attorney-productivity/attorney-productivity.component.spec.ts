import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AttorneyProductivityComponent } from './attorney-productivity.component';

describe('AttorneyProductivityComponent', () => {
  let component: AttorneyProductivityComponent;
  let fixture: ComponentFixture<AttorneyProductivityComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AttorneyProductivityComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AttorneyProductivityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
