package com.seminario.backend.repository.bienes;

import com.seminario.backend.model.abm.Estado;
import com.seminario.backend.model.bienes.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    Movimiento findById(Long id);
    List<Movimiento> findByTipoMovimientoAndEstadoViajeAndDestino(TipoMovimiento tipo, EstadoViaje estado, Long nroLocalDestino);

    @Query(value = "select * from Movimiento where (origen = ?1 or destino = ?1 or ?1 is null)" , nativeQuery = true)
    List<Movimiento> findAllByLocal(Long nro);


    @Query(value = "select * from Movimiento where (ESTADOVIAJE_ID != 2) and (origen = ?1 or destino = ?1 or ?1 is null)" +
            " and (fechasalida between ?2  and ?3) " , nativeQuery = true)
    List<Movimiento> findAllByLocalAndFecha(Long nro, Date fecha_desde, Date fecha_hasta );

    @Query("SELECT m FROM Movimiento m \n" +
            "WHERE (m.fechaSalida > ?1 or ?1 IS NULL) \n" +
            "AND (m.fechaSalida < ?2 or ?2 IS NULL) \n" +
            "AND (m.fechaAlta > ?3 or ?3 IS NULL)\n" +
            "AND (m.fechaAlta < ?4 or ?4 IS NULL)\n" +
            "AND (m.origen = ?5 or ?5 IS NULL)\n" +
            "AND (m.destino = ?6 or ?6 IS NULL)\n" +
            "AND (m.tipoMovimiento = ?7 or ?7 IS NULL)\n" +
            "AND (m.tipoMovimiento.tipoAgenteOrigen = ?8 or ?8 IS NULL)\n" +
            "AND (m.tipoMovimiento.tipoAgenteDestino = ?9 or ?9 IS NULL)\n" +
            "AND (m.tipoDocumento = ?10 or ?10 is null)\n" +
            "AND (m.usuarioAlta = ?11 or ?11 IS NULL)\n" +
            "AND (m.estadoViaje = ?12 or ?12 IS NULL)\n" +
            "AND (m.idTransportista = ?13 or ?13 IS NULL)\n" +
            "AND (EXISTS(SELECT r FROM m.recursosAsignados r WHERE r.estadoRecurso = ?14) or ?14 IS NULL)\n" +
            "AND (EXISTS(SELECT r FROM m.recursosAsignados r WHERE r.nroRecurso = ?15) or ?15 IS NULL)\n" +
            "AND (EXISTS(SELECT im FROM m.itemMovimientos im WHERE im.estadoRecurso = ?16) or ?16 IS NULL)\n" +
            "AND (EXISTS(SELECT im FROM m.itemMovimientos im WHERE im.cantidad = ?17) or ?17 IS NULL)\n" +
            "AND (EXISTS(SELECT im FROM m.itemMovimientos im WHERE im.bienIntercambiable = ?18) or ?18 IS NULL)\n"
            )
    List<Movimiento> findAllComplex(
            Date fechaSalidaDesde,
            Date fechaSalidaHasta,
            Date fechaAltaDesde,
            Date fechaAltaHasta,
            Long origen,
            Long destino,
            TipoMovimiento tipoMovimiento,
            TipoAgente tipoAgenteOrigen,
            TipoAgente tipoAgenteDestino,
            TipoDocumento tipoDocumento,
            String usuarioAlta,
            EstadoViaje estadoViaje,
            Long idTransportista,
            EstadoRecurso estadoRecursoRecurso,
            Long nroRecurso,
            EstadoRecurso estadoRecursoItemMovimiento,
            Long cantidadItemMovimiento,
            BienIntercambiable bienIntercambiable
    );
}
