import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OverFiewSongComponent } from './over-fiew-song.component';

describe('OverFiewSongComponent', () => {
  let component: OverFiewSongComponent;
  let fixture: ComponentFixture<OverFiewSongComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OverFiewSongComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OverFiewSongComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
