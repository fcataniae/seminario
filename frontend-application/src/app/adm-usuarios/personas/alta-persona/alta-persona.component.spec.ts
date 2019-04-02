import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AltaPersonaComponent } from './alta-persona.component';

describe('AltaPersonaComponent', () => {
  let component: AltaPersonaComponent;
  let fixture: ComponentFixture<AltaPersonaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AltaPersonaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AltaPersonaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
