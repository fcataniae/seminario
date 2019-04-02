import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ModificarPersonaComponent } from './modificar-persona.component';

describe('ModificarPersonaComponent', () => {
  let component: ModificarPersonaComponent;
  let fixture: ComponentFixture<ModificarPersonaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ModificarPersonaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ModificarPersonaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
