import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MatterlistComponent } from './matterlist.component';

describe('MatterlistComponent', () => {
  let component: MatterlistComponent;
  let fixture: ComponentFixture<MatterlistComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MatterlistComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MatterlistComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
