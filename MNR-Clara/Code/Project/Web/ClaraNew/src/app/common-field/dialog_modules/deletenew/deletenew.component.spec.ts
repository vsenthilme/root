import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeletenewComponent } from './deletenew.component';

describe('DeletenewComponent', () => {
  let component: DeletenewComponent;
  let fixture: ComponentFixture<DeletenewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DeletenewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DeletenewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
