package com.seminario.services;

import com.seminario.backend.BackendApplication;
import com.seminario.backend.model.abm.Estado;
import com.seminario.backend.model.abm.Usuario;
import com.seminario.backend.model.bienes.*;
import com.seminario.backend.repository.bienes.BienIntercambiableRepository;
import com.seminario.backend.repository.bienes.TipoMovimientoRepository;
import com.seminario.backend.services.abm.UsuarioService;
import com.seminario.backend.services.bienes.MovimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.util.*;

/**
 * UserAuth: fcatania
 * Date: 27/3/2019
 * Time: 08:42
 */
@SpringBootApplication
@Import({BackendApplication.class})
@ComponentScan({"com.seminario.services"})
public class WebserviceApplication  extends SpringBootServletInitializer implements CommandLineRunner {

    public static void main(String[] args){
        SpringApplication.run(WebserviceApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(WebserviceApplication.class);
    }


    @Override
    public void run(String... args) throws Exception {
        List<Date> fechas = new ArrayList<>();
        int d = 365;
        for (int i = 1 ; i< d; i++){
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DAY_OF_YEAR,-i);
            fechas.add(c.getTime());
        }
        int o[] = {1,2,3,4,5,6};
        int ob[] = {1,2,3,4,5,6,7,8,9,10};
        Random random = new Random();
        for(int j = 1; j <= 3; j++){
        int i2 = 0;
            for(Date fecha : fechas){

                Movimiento m = new Movimiento();
                EstadoViaje e = new EstadoViaje();
                e.setId((long)j); //1 pendiente 2 entregado 3 cancelado
                m.setEstadoViaje(e);

                m.setDestino(7460L);
                m.setOrigen((long)o[random.nextInt(o.length)]);
                m.setTipoMovimiento(t.getOne(3L));
                Estado e2 = new Estado();
                e2.setId(1L);
                m.setEstado(e2);
                m.setFechaSalida(fecha);
                m.setFechaAlta(fecha);
                m.setNroDocumento("abc123-"+i2);
                m.setUsuarioAlta("admin");
                TipoDocumento t = new TipoDocumento();
                t.setId(9L);
                m.setTipoDocumento(t);
                ItemMovimiento it= new ItemMovimiento();

                it.setBienIntercambiable(b.findById((long)ob[random.nextInt(ob.length)]));
                EstadoRecurso er = new EstadoRecurso();
                er.setId(2L);
                er.setDescrip("OCUPADO");
                it.setEstadoRecurso(er);
                ItemMovimientoTipoDoc itd = new ItemMovimientoTipoDoc();
                TipoDocumento t2 = new TipoDocumento();
                t2.setId(9L);
                itd.setTipoDocumento(t2);
                itd.setNroDocumento("asd-123-" + i2);
                it.setItemMovimientoTipoDoc(new HashSet<>());
                it.getItemMovimientoTipoDoc().add(itd);
                it.setCantidad((long)random.nextInt(200));
                m.setItemMovimientos(new HashSet<>());
                m.getItemMovimientos().add(it);
                i2++;
                m.setRecursosAsignados(new HashSet<>());
                Usuario u = usuario.getUsuarioByNombre("admin");
                movimiento.create(u,m);
            }
        }
        int od[] = {1023,1024,1025,1002,1003,1004,1005,1006};
        random = new Random();
        for(int j = 1; j <= 6; j++){
            int i2 = 0;
            for(Date fecha : fechas){
                try {
                    Movimiento m = new Movimiento();
                    EstadoViaje e = new EstadoViaje();
                    e.setId((long) (j % 4) + 1); //1 pendiente 3 entregado 2 cancelado
                    m.setEstadoViaje(e);

                    m.setDestino((long) od[random.nextInt(od.length)]);
                    m.setOrigen(7460L);
                    m.setTipoMovimiento(t.getOne(1L));
                    Estado e2 = new Estado();
                    e2.setId(1L);
                    m.setEstado(e2);

                    m.setFechaSalida(fecha);
                    m.setFechaAlta(fecha);
                    m.setNroDocumento("JRE223-" + i2);
                    m.setUsuarioAlta("admin");
                    TipoDocumento t = new TipoDocumento();
                    t.setId(8L);
                    m.setTipoDocumento(t);
                    ItemMovimiento it = new ItemMovimiento();

                    it.setBienIntercambiable(b.findById((long) ob[random.nextInt(ob.length)]));
                    EstadoRecurso er = new EstadoRecurso();
                    er.setId(2L);
                    er.setDescrip("OCUPADO");
                    it.setEstadoRecurso(er);
                    //ItemMovimientoTipoDoc itd = new ItemMovimientoTipoDoc();
                    //TipoDocumento t2 = new TipoDocumento();
                    //t2.setId(9L);
                    //itd.setTipoDocumento(t2);
                    //itd.setNroDocumento("JKA2-" + i2);
                    it.setItemMovimientoTipoDoc(new HashSet<>());
                    // it.getItemMovimientoTipoDoc().add(itd);
                    it.setCantidad((long) random.nextInt(100));
                    m.setItemMovimientos(new HashSet<>());
                    m.getItemMovimientos().add(it);
                    if (random.nextInt(100) > 50) {
                        long b2 = (long) ob[random.nextInt(ob.length)];
                        BienIntercambiable bi = b.findById((b2 == it.getBienIntercambiable().getId()) ? 1L : b2);
                        it = new ItemMovimiento();
                        it.setBienIntercambiable(bi);
                        it.setEstadoRecurso(er);
                        it.setCantidad((long) random.nextInt(100));
                        it.setItemMovimientoTipoDoc(new HashSet<>());
                        m.getItemMovimientos().add(it);
                    }
                    String patentes[] = {"ADS 123", "BS 123 KS", "KKK 333", "JKE 005", "BB 123 EW", "OPQ 423"};
                    m.setPatenteTransporte(patentes[random.nextInt(patentes.length)]);
                    int transp[] = {5043343, 5043344, 5043345, 5043346, 5043347, 5043348, 5043349, 5043350};
                    m.setIdTransportista((long) transp[random.nextInt(transp.length)]);
                    i2++;
                    m.setRecursosAsignados(new HashSet<>());
                    Usuario u = usuario.getUsuarioByNombre("admin");
                    movimiento.create(u, m);
                }catch(Exception e){
                    //si entra aca seguro es porque no tiene stock del bien, por eso lo dejo seguir pasando
                    System.out.println(e.getMessage());
                    System.out.println("Algo paso, igual sigo prbando");
                }
                int o2 = 0;
                List<Movimiento> movs = movimiento.getAllMovimientos(usuario.getUsuarioByNombre("admin"));
                for(Movimiento mov : movs) {
                    if(mov.getTipoMovimiento().getTipo() =="ENVIO" && o2 % 2 == 0){
                        mov.setComentario("Cancelado porque algo paso en el viaje y no se pudo completar (?");
                        movimiento.cambiarEstadoMovimiento(usuario.getUsuarioByNombre("admin"),mov.getId(),"CANCELADO", mov.getComentario() + o2);
                    }
                    o2++;
                }

            }
        }
    }

    @Autowired
    MovimientoService movimiento;
    @Autowired
    TipoMovimientoRepository t;
    @Autowired
    BienIntercambiableRepository b;
    @Autowired
    UsuarioService usuario;
}