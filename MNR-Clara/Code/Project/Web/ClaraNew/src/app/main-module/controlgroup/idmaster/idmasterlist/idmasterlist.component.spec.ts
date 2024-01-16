import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IdmasterlistComponent } from './idmasterlist.component';

describe('IdmasterlistComponent', () => {
  let component: IdmasterlistComponent;
  let fixture: ComponentFixture<IdmasterlistComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IdmasterlistComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IdmasterlistComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
