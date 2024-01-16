import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MaterialNewComponent } from './material-new.component';

describe('MaterialNewComponent', () => {
  let component: MaterialNewComponent;
  let fixture: ComponentFixture<MaterialNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MaterialNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MaterialNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
