package com.seminario.backend.services.bienes;

import com.seminario.backend.model.bienes.TipoAgente;
import com.seminario.backend.repository.abm.PermisoRepository;
import com.seminario.backend.repository.bienes.LocalRepository;
import com.seminario.backend.model.bienes.Local;
import com.seminario.backend.model.abm.Usuario;
import com.seminario.backend.repository.bienes.TipoAgenteRepository;
import com.seminario.backend.services.abm.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocalService {

    @Autowired
    LocalRepository localRepository;

    @Autowired
    PermisoRepository permisoRepository;

    @Autowired
    TipoAgenteRepository tipoAgenteRepository;

    // quedaria getAgentes en una nueva version.
    public List<Local> getLocales (Usuario usuarioActual){
        if (null != permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(),"CONS-LOCAL")) {
            return localRepository.findAll();
        } else {
            throw new CustomException("No cuenta con los permisos para consultar locales");
        }
    }


    public List<Local> getTiendas (Usuario usuarioActual){
        if (null != permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(),"CONS-LOCAL")) {
            TipoAgente tienda  = tipoAgenteRepository.findByNombre("LOCAL");
            return localRepository.findAllByTipoAgente(tienda);
        } else {
            throw new CustomException("No cuenta con los permisos para consultar tiendas");
        }
    }

    public List<Local> getProveedores (Usuario usuarioActual){
        if (null != permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(),"CONS-LOCAL")) {
            TipoAgente tipoProveedor  = tipoAgenteRepository.findByNombre("PROVEEDOR");
            return localRepository.findAllByTipoAgente(tipoProveedor);
        } else {
            throw new CustomException("No cuenta con los permisos para consultar proveedores");
        }
    }

    public List<Local> getCDs (Usuario usuarioActual){
        if (null != permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(),"CONS-LOCAL")) {
            TipoAgente tipoCD  = tipoAgenteRepository.findByNombre("CENTRODISTR");
            return localRepository.findAllByTipoAgente(tipoCD);
        } else {
            throw new CustomException("No cuenta con los permisos para consultar centros de distribucion");
        }
    }
}