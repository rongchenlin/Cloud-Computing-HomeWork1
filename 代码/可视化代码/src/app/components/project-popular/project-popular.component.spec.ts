import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectPopularComponent } from './project-popular.component';

describe('ProjectPopularComponent', () => {
  let component: ProjectPopularComponent;
  let fixture: ComponentFixture<ProjectPopularComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProjectPopularComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProjectPopularComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
