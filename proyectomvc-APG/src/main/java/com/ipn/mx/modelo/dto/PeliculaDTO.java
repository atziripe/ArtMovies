package com.ipn.mx.modelo.dto;

import com.ipn.mx.modelo.entidades.Pelicula;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author El TEAM AFR
 */
@Data
@AllArgsConstructor
public class PeliculaDTO implements Serializable {
    private Pelicula entidad;
    
    public PeliculaDTO(){
        entidad = new Pelicula();
    }
}
