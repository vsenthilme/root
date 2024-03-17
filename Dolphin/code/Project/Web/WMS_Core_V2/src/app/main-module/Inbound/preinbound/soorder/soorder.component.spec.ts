import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SoorderComponent } from './soorder.component';

describe('SoorderComponent', () => {
  let component: SoorderComponent;
  let fixture: ComponentFixture<SoorderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SoorderComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SoorderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
