import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InterwarehousetransferaddlinesComponent } from './interwarehousetransferaddlines.component';

describe('InterwarehousetransferaddlinesComponent', () => {
  let component: InterwarehousetransferaddlinesComponent;
  let fixture: ComponentFixture<InterwarehousetransferaddlinesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InterwarehousetransferaddlinesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InterwarehousetransferaddlinesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
