package com.seminario.backend.repository.abm;

import com.seminario.backend.model.abm.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * User: fcatania
 * Date: 1/4/2019
 * Time: 09:18
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long>  {
    @Query("SELECT u FROM Usuario u WHERE u.nombreUsuario = ?1 and u.password = ?2")
    Usuario findByNombreUsuarioPassword(String nombreUsuario, String password);
    Usuario findByNombreUsuario(String name);
    Usuario findById(Long Id);
}