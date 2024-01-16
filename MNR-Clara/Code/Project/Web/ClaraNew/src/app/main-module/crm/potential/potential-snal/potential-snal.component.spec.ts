import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PotentialSnalComponent } from './potential-snal.component';

describe('PotentialSnalComponent', () => {
  let component: PotentialSnalComponent;
  let fixture: ComponentFixture<PotentialSnalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PotentialSnalComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PotentialSnalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});