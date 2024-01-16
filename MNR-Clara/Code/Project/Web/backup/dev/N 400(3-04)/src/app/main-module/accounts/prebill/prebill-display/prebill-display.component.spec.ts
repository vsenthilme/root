import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PrebillDisplayComponent } from './prebill-display.component';

describe('PrebillDisplayComponent', () => {
  let component: PrebillDisplayComponent;
  let fixture: ComponentFixture<PrebillDisplayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PrebillDisplayComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PrebillDisplayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
