import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MattersTabComponent } from './matters-tab.component';

describe('MattersTabComponent', () => {
  let component: MattersTabComponent;
  let fixture: ComponentFixture<MattersTabComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MattersTabComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MattersTabComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
