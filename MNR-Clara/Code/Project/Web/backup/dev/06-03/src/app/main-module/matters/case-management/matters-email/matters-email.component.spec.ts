import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MattersEmailComponent } from './matters-email.component';

describe('MattersEmailComponent', () => {
  let component: MattersEmailComponent;
  let fixture: ComponentFixture<MattersEmailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MattersEmailComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MattersEmailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
