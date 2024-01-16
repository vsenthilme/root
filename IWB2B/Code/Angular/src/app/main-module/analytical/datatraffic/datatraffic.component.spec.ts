import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DatatrafficComponent } from './datatraffic.component';

describe('DatatrafficComponent', () => {
  let component: DatatrafficComponent;
  let fixture: ComponentFixture<DatatrafficComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DatatrafficComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DatatrafficComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
