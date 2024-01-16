import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PrebillComponent } from './prebill.component';

describe('PrebillComponent', () => {
  let component: PrebillComponent;
  let fixture: ComponentFixture<PrebillComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PrebillComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PrebillComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
