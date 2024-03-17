import { ComponentFixture, TestBed } from '@angular/core/testing';

import { D3CollapsibleComponent } from './d3-collapsible.component';

describe('D3CollapsibleComponent', () => {
  let component: D3CollapsibleComponent;
  let fixture: ComponentFixture<D3CollapsibleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ D3CollapsibleComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(D3CollapsibleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
