import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConflickCheckMainComponent } from './conflick-check-main.component';

describe('ConflickCheckMainComponent', () => {
  let component: ConflickCheckMainComponent;
  let fixture: ComponentFixture<ConflickCheckMainComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConflickCheckMainComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ConflickCheckMainComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
