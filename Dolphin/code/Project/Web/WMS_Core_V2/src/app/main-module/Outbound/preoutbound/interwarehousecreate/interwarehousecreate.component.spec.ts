import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InterwarehousecreateComponent } from './interwarehousecreate.component';

describe('InterwarehousecreateComponent', () => {
  let component: InterwarehousecreateComponent;
  let fixture: ComponentFixture<InterwarehousecreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InterwarehousecreateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InterwarehousecreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
