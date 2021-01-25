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
import com.ipn.mx.utilerias.Utilerias;
import java.awt.event.ActionEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author Atziri Perez
 */
@ManagedBean(name = "UsuarioMB")
@SessionScoped
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioMB extends BaseBean implements Serializable {

    private final UsuarioDAO dao = new UsuarioDAO();
    private UsuarioDTO dto;
    private List<UsuarioDTO> listaUsuarios;
    private UploadedFile file;

    /**
     * Creates a new instance of UsuarioMB
     */

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
    
    public String prepareListaU(){
        init();
        return "/listaUsuarios?faces-redirect=true";
    }

    public String prepareIndex() {
        init();
        return "/login?faces-redirect=true";
    }

    public String prepareIndexA() {
        init();
        return "/inicioAN?faces-redirect=true";
    }

    public String back() {
        return prepareIndex();
    }

    public boolean validate() {
        boolean valido = true;
        return valido;
    }

    public String add() throws IOException {
        Boolean valido = validate();
        if (valido) {
            InputStream input = file.getInputStream();
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[64000];
            for (int i = 0; (i = input.read(buffer)) > 0;) {
                output.write(buffer, 0, i);
            }
            dto.getEntidad().setImagen(output.toByteArray());
            dao.create(dto);
            Utilerias mandarCorreo = new Utilerias();
            String destinatario = dto.getEntidad().getEmail();
            String asunto = "HOLA! GRACIAS POR REGISTRARTE";
            String texto = "Bienvenido a nuestro proyecto final de Web Application Development :)";
            mandarCorreo.enviarCorreo(destinatario, asunto, texto);
            if (valido) {
                file = null;
                return prepareIndex();
            } else {
                return prepareAdd();
            }
        } else {
        }
        return prepareAdd();
    }

//    public String verify() {
//        //boolean valido = validate();
//        if (dao.isVerify(dao.verify(dto.getEntidad().getNombreUsuario(), dto.getEntidad().getClaveUsuario()))) {
//            return prepareIndexA();
//        } else {
//            return prepareIndex();
//        }
//    }

    public String update() throws IOException {
        boolean valido = validate();
        if (valido) {
            InputStream input = file.getInputStream();
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[64000];
            for (int i = 0; (i = input.read(buffer)) > 0;) {
                output.write(buffer, 0, i);
            }
            dto.getEntidad().setImagen(output.toByteArray());
            dao.update(dto);
            Utilerias mandarCorreo = new Utilerias();
            String destinatario = dto.getEntidad().getEmail();
            String asunto = "¿ACTUALIZASTE TUS DATOS?";
            String texto = "Tenemos registro de una actualización de tus datos en el sistema ¿Fuiste tú?";
            mandarCorreo.enviarCorreo(destinatario, asunto, texto);
            return prepareIndexA();
        } else {
            return prepareUpdate();
        }
    }

    public String delete() {
        dao.delete(dto);
        return prepareListaU();
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

    public int idUsu() {
        int idUser = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idUser");
        return idUser;
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

    public StreamedContent getImage() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            return new DefaultStreamedContent();
        } else {
            String id = context.getExternalContext().getRequestParameterMap().get("id");
            return new DefaultStreamedContent(new ByteArrayInputStream(dto.getEntidad().getImagen()), "image/*");
        }
    }

}
