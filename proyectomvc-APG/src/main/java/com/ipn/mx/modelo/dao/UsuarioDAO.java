/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipn.mx.modelo.dao;

import com.ipn.mx.modelo.dto.UsuarioDTO;
import com.ipn.mx.modelo.entidades.Usuario;
import com.ipn.mx.utilerias.HibernateUtil;
import com.ipn.mx.utilerias.Utilerias;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.sql.DataSource;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.ipn.mx.controlador.web.UsuarioMB;
import com.ipn.mx.utilerias.Conexion;
import java.util.logging.Level;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 *
 * @author El TEAM AFR
 */
public class UsuarioDAO {
    
    public void create(UsuarioDTO dto) {
        Session sesion = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = sesion.getTransaction();
        try {
            transaction.begin();
            sesion.save(dto.getEntidad());
            transaction.commit();
        } catch (HibernateException he) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
        }
    }

    public void update(UsuarioDTO dto) {
        Session sesion = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = sesion.getTransaction();
        try {
            transaction.begin();
            sesion.update(dto.getEntidad());
            transaction.commit();
        } catch (HibernateException he) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
        }
    }

    public void delete(UsuarioDTO dto) {
        Session sesion = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = sesion.getTransaction();
        try {
            transaction.begin();
            sesion.delete(dto.getEntidad());
            transaction.commit();
        } catch (HibernateException he) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
        }
    }

    public UsuarioDTO read(UsuarioDTO dto) {
        Session sesion = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = sesion.getTransaction();
        try {
            transaction.begin();
            //select * from usuario where idUsuario = ?
            dto.setEntidad(sesion.get(dto.getEntidad().getClass(), dto.getEntidad().getIdUsuario()));
            //dto.setEntidad(dto.getEntidad());
            transaction.commit();
        } catch (HibernateException he) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
        }
        return dto;
    }

    public List readAll() {
        Session sesion = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = sesion.getTransaction();
        List lista = new ArrayList();
        try {
            transaction.begin();
            //select * from Usuario u order by u.idUsuario;
            //              Usuario u
            Query q = sesion.createQuery("from Usuario u order by u.idUsuario");
            for (Usuario u : (List<Usuario>) q.list()) {
                UsuarioDTO dto = new UsuarioDTO();
                dto.setEntidad(u);
                lista.add(dto);
            }
            transaction.commit();
        } catch (HibernateException he) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
        }
        return lista;
    }

    public Usuario verify(String usuario, String clave) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.getTransaction();
        Usuario u = null;
        try {
            transaction.begin();
            Query q = session.createQuery("from Usuario as u "
                    + "where u.nombreUsuario =:usuario  and u.claveUsuario =:clave ");
            q.setParameter("usuario", usuario);
            q.setParameter("clave", clave);
            List l = q.list();
            if (l.size() > 0) {
                u = (Usuario) l.get(0);
            } else {
                return null;
            }
            transaction.commit();
        } catch (HibernateException he) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
        }
        return u;
    }
    
    public boolean isVerify(Usuario u){
        return u != null;
    }

    public int login(String user, String pass) throws SQLException {
        Conexion conect = new Conexion();
        Connection cone = conect.conecta();
        PreparedStatement ps = null;
        try {
            ps = cone.prepareStatement(
                    "SELECT idUsuario, nombreusuario, claveusuario FROM usuario WHERE nombreusuario= ? and claveusuario= ? ");
            ps.setString(1, user);
            ps.setString(2, pass);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) // found
            {
                //System.out.println(rs.getString("email"));
                int id = rs.getInt("idUsuario");
                return id;
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "LoginDAO!",
                        "Wrong password message test!"));
                return 0;
            }
        } catch (SQLException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Database Error",
                    "Unable to connect database"));
            System.out.println("Error in login() -->" + ex.getMessage());
            return 0;
        } finally {
        }
    }
    
    public static void main(String[] args) {
        UsuarioDAO dao = new UsuarioDAO();
        UsuarioDTO dto = new UsuarioDTO();

 //       dto.getEntidad().setIdUsuario(1);

        dto.getEntidad().setNombre("batman3");
        dto.getEntidad().setPaterno("batman");
        dto.getEntidad().setMaterno("batman");
        dto.getEntidad().setEmail("batman@baticueva.com");
        dto.getEntidad().setNombreUsuario("SrR");
        dto.getEntidad().setClaveUsuario("12345>");
        dto.getEntidad().setTipoUsuario("N");
//        try {
//            //int res = dao.login("SrR", "12345>");
//            int res = dao.login("AtPG", "54321>");
//            System.out.println(res);
//        } catch (SQLException ex) {
//            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
//        }

//        dao.update(dto);
//        dao.delete(dto);
        dao.create(dto);
        
//ystem.out.println(dao.readAll());
//System.out.println(dao.read(dto));

//System.out.println(dao.isVerify(dao.verify(dto.getEntidad().getNombreUsuario(), dto.getEntidad().getClaveUsuario())));
//        if (dao.findByUserNameAndPassword(dto.getEntidad().getNombreUsuario(), dto.getEntidad().getClaveUsuario()) != null){
//            Utilerias util = new Utilerias();
//            util.enviarCorreo("asuncionez@gmail.com", "mensaje", "trexto");
//}
    }

}
