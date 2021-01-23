/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipn.mx.controlador.web;

import static com.ipn.mx.controlador.web.BaseBean.ACC_ACTUALIZAR;
import static com.ipn.mx.controlador.web.BaseBean.ACC_CREAR;
import com.ipn.mx.modelo.dao.UsuarioDAO;
import com.ipn.mx.modelo.dto.UsuarioDTO;
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
@ManagedBean(name = "UsuarioMB")
@SessionScoped
public class UsuarioMB extends BaseBean implements Serializable {

    private final UsuarioDAO dao = new UsuarioDAO();
    private UsuarioDTO dto;
    private List<UsuarioDTO> listaUsuarios;
    private String username;
    private String password;

    /**
     * Creates a new instance of UsuarioMB
     */
    public UsuarioMB() {
    }

    @PostConstruct
    public void init() {
        listaUsuarios = new ArrayList<>();
        listaUsuarios = dao.readAll();
    }

    public String prepareAdd() {
        dto = new UsuarioDTO();
        setAccion(ACC_CREAR);
        return "/registro?faces-redirect=true";
    }

    public String prepareVerify() {
        dto = new UsuarioDTO();
        setAccion(ACC_VERIFICAR);
        return "/login?faces-redirect=true";
    }

    public String prepareUpdate() {
        setAccion(ACC_ACTUALIZAR);
        return "/updateUser?faces-redirect=true";
    }

    public String prepareIndex() {
        init();
        return "/index?faces-redirect=true";
    }

    public String prepareIndexA() {
        init();
        return "/inicioAdmin?faces-redirect=true";
    }

    public String back() {
        return prepareIndex();
    }

    public boolean validate() {
        boolean valido = true;
        //las validaciones
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

    public String verify() {
        //boolean valido = validate();
        if (dao.isVerify(dao.verify(dto.getEntidad().getNombreUsuario(), dto.getEntidad().getClaveUsuario()))) {
            return prepareIndexA();
        } else {
            return prepareIndex();
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

    public void seleccionarUsuario(ActionEvent event) {
        String claveSel = (String) FacesContext.getCurrentInstance().
                getExternalContext().
                getRequestParameterMap().get("claveSel");
        dto = new UsuarioDTO();
        dto.getEntidad().setIdUsuario(Integer.parseInt(claveSel));
        try {
            dao.read(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UsuarioDTO getDto() {
        return dto;
    }

    public void setDto(UsuarioDTO dto) {
        this.dto = dto;
    }

    public List<UsuarioDTO> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(List<UsuarioDTO> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public String nombreusu() {
        String user = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
        return user;
    }

    public void verificarsesion() {
        try {
            String user = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
            if (user == null) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cerrarsesion() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    }

}
