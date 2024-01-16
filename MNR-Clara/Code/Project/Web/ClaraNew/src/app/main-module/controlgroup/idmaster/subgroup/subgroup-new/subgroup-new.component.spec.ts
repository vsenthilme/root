import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubgroupNewComponent } from './subgroup-new.component';

describe('SubgroupNewComponent', () => {
  let component: SubgroupNewComponent;
  let fixture: ComponentFixture<SubgroupNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SubgroupNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SubgroupNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
