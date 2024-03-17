import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InterwarehousetransferComponent } from './interwarehousetransfer.component';

describe('InterwarehousetransferComponent', () => {
  let component: InterwarehousetransferComponent;
  let fixture: ComponentFixture<InterwarehousetransferComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InterwarehousetransferComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InterwarehousetransferComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
