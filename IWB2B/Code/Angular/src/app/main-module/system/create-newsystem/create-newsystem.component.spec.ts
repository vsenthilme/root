import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateNewsystemComponent } from './create-newsystem.component';

describe('CreateNewsystemComponent', () => {
  let component: CreateNewsystemComponent;
  let fixture: ComponentFixture<CreateNewsystemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateNewsystemComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateNewsystemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
