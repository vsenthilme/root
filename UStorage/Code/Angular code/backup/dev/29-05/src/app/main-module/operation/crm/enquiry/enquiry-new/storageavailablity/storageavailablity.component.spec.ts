import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StorageavailablityComponent } from './storageavailablity.component';

describe('StorageavailablityComponent', () => {
  let component: StorageavailablityComponent;
  let fixture: ComponentFixture<StorageavailablityComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StorageavailablityComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StorageavailablityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
