import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ModificarUsuarioComponent } from './modificar-usuario.component';

describe('ModificarUsuarioComponent', () => {
  let component: ModificarUsuarioComponent;
  let fixture: ComponentFixture<ModificarUsuarioComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ModificarUsuarioComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ModificarUsuarioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
