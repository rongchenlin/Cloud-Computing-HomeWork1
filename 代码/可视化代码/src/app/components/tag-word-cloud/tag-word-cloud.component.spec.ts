import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TagWordCloudComponent } from './tag-word-cloud.component';

describe('TagWordCloudComponent', () => {
  let component: TagWordCloudComponent;
  let fixture: ComponentFixture<TagWordCloudComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TagWordCloudComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TagWordCloudComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
