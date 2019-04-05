package com.seminario.backend.services.interfaces;

import com.seminario.backend.model.Estado;

public interface IEstadoService {
    Estado getEstadoByNombre(String nombre);
}
