import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArchivesLogComponent } from './archives-log.component';

describe('ArchivesLogComponent', () => {
  let component: ArchivesLogComponent;
  let fixture: ComponentFixture<ArchivesLogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ArchivesLogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ArchivesLogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
