import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InformeTiendasComponent } from './informe-tiendas.component';

describe('InformeTiendasComponent', () => {
  let component: InformeTiendasComponent;
  let fixture: ComponentFixture<InformeTiendasComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InformeTiendasComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InformeTiendasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
