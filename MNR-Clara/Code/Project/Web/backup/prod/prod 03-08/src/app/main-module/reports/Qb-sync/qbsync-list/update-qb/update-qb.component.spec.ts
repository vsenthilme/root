import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateQbComponent } from './update-qb.component';

describe('UpdateQbComponent', () => {
  let component: UpdateQbComponent;
  let fixture: ComponentFixture<UpdateQbComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UpdateQbComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UpdateQbComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
