import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QatarPostComponent } from './qatar-post.component';

describe('QatarPostComponent', () => {
  let component: QatarPostComponent;
  let fixture: ComponentFixture<QatarPostComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ QatarPostComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(QatarPostComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
