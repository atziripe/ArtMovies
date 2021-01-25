/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipn.mx.controlador.web;

import com.ipn.mx.modelo.dao.PeliculaDAO;
import com.ipn.mx.modelo.dto.GeneroDTO;
import com.ipn.mx.modelo.dto.PeliculaDTO;
import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Atziri Perez
 */
@ManagedBean(name = "PeliculaMB")
@SessionScoped
public class PeliculaMB extends BaseBean implements Serializable {

    private final PeliculaDAO dao = new PeliculaDAO();
    private PeliculaDTO dto;
    private List<PeliculaDTO> listaPeliculas;

    /**
     * Creates a new instance of PeliculaMB
     */
    public PeliculaMB() {
    }

    @PostConstruct
    public void init() {
        listaPeliculas = new ArrayList<>();
        listaPeliculas = dao.readAll();
    }

    public String prepareAdd() {
        dto = new PeliculaDTO();
        setAccion(ACC_CREAR);
        return "/peliculaForm?faces-redirect=true";
    }

    public String prepareRead() {
        setAccion(ACC_LEER);
        return "/verPelicula?faces-redirect=true";
    }
    
    public String prepareUpdate() {
        setAccion(ACC_ACTUALIZAR);
        return "/peliculaForm?faces-redirect=true";
    }

    public String prepareIndex() {
        init();
        return "/listaPeliculas?faces-redirect=true";
    }

    public String back() {
        return prepareIndex();
    }

    public boolean validate() {
        boolean valido = true;
        return valido;
    }

    public String add() {
        boolean valido = validate();
        if (valido) {
            dao.create(dto);
            return prepareIndex();
        } else {
            return prepareAdd();
        }
    }

    public String update() {
        boolean valido = validate();
        if (valido) {
            dao.update(dto);
            return prepareIndex();
        } else {
            return prepareUpdate();
        }
    }

    public String delete() {
        dao.delete(dto);
        return prepareIndex();
    }
    
    public String voteP() {
        boolean valido = validate();
        if (valido) {
            int vp = dto.getEntidad().getVotosPositivos();
            dto.getEntidad().setVotosPositivos(vp+1);
            dao.update(dto);
            return prepareIndex();
        } else {
            return prepareUpdate();
        }
    }
    
    public String voteN() {
        boolean valido = validate();
        if (valido) {
            int vn = dto.getEntidad().getVotosNegativos();
            dto.getEntidad().setVotosNegativos(vn+1);
            dao.update(dto);
            return prepareIndex();
        } else {
            return prepareUpdate();
        }
    }
    
    public void seleccionarPelicula(ActionEvent event) {
        String claveSel = (String) 
                FacesContext.getCurrentInstance().
                        getExternalContext().
                        getRequestParameterMap().get("claveSel");
        dto = new PeliculaDTO();
        dto.getEntidad().setIdPelicula(Integer.parseInt(claveSel));
        try {
            dao.read(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PeliculaDTO getDto() {
        return dto;
    }

    public void setDto(PeliculaDTO dto) {
        this.dto = dto;
    }

    public List<PeliculaDTO> getListaPeliculas() {
        return listaPeliculas;
    }

    public void setListaPeliculas(List<PeliculaDTO> listaPeliculas) {
        this.listaPeliculas = listaPeliculas;
    }
}
