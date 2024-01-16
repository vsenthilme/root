import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MatterPlComponent } from './matter-pl.component';

describe('MatterPlComponent', () => {
  let component: MatterPlComponent;
  let fixture: ComponentFixture<MatterPlComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MatterPlComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MatterPlComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
