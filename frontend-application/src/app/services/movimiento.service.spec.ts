import { TestBed } from '@angular/core/testing';

import { MovimientoService } from './movimiento.service';

describe('MovimientoService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: MovimientoService = TestBed.get(MovimientoService);
    expect(service).toBeTruthy();
  });
});
