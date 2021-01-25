/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipn.mx.controlador;

import com.ipn.mx.modelo.dao.GraficaDAO;
import com.ipn.mx.utilerias.Conexion;

import com.ipn.mx.modelo.dao.UsuarioDAO;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
/**
 *
 * @author TEAM
 */
@WebServlet(name = "utileriasServlet", urlPatterns = {"/utileriasServlet"})
public class utileriasServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        String accion = request.getParameter("accion");
        if (accion.equals("grafica")) {
            graficar(request, response);
        } else {
            if (accion.equals("reportePeli")) {
                verPDFPeli(request, response);
            }else{
                if(accion.equals("reporteGen")){
                    verPDFGen(request,response);
                }else{
                    if(accion.equals("reporteU")){
                        verPDFUsuario(request, response);
                    }else{
                        if(accion.equals("reporteUs")){
                            verPDFUsuarios(request,response);
                        }
                    }
                }
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(utileriasServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(utileriasServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void graficar(HttpServletRequest request, HttpServletResponse response) {
        GraficaDAO dao = new GraficaDAO();

        RequestDispatcher rd = request.getRequestDispatcher("graficaTodo.jsp");

        List listaPelixGen = dao.graficaPeliXGen();
        List listaGenVotado = dao.graficaGenVotado();
        List listaGenNoVotado = dao.graficaGenNoVotado();
        List listaDuracion = dao.graficaDuracion();
        List listaPeliClas = dao.graficaPelixClasificacion();

        request.setAttribute("peliGen", listaPelixGen);
        request.setAttribute("genV", listaGenVotado);
        request.setAttribute("genNV", listaGenNoVotado);
        request.setAttribute("dura", listaDuracion);
        request.setAttribute("peliClas", listaPeliClas);
        try {
            rd.forward(request, response);
        } catch (ServletException ex) {
            Logger.getLogger(utileriasServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(utileriasServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void verPDFGen(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        Conexion con = new Conexion();
        try {
            ServletOutputStream sos = response.getOutputStream();
            File reporte = new File(getServletConfig().getServletContext().getRealPath("/reportes/LosGeneros.jasper"));
            byte[] bytes = JasperRunManager.runReportToPdf(reporte.getPath(), null, con.conecta());
            response.setContentType("application/pdf");
            response.setContentLength(bytes.length);
            sos.write(bytes, 0, bytes.length);
            sos.flush();
            sos.close();
        } catch (IOException | JRException ex) {
            Logger.getLogger(utileriasServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
    private void verPDFPeli(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        Conexion con = new Conexion();
        try {
            ServletOutputStream sos = response.getOutputStream();
            File reporte = new File(getServletConfig().getServletContext().getRealPath("/reportes/Peliculas.jasper"));
            byte[] bytes = JasperRunManager.runReportToPdf(reporte.getPath(), null, con.conecta());
            response.setContentType("application/pdf");
            response.setContentLength(bytes.length);
            sos.write(bytes, 0, bytes.length);
            sos.flush();
            sos.close();
        } catch (IOException | JRException ex) {
            Logger.getLogger(utileriasServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void verPDFUsuarios(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        Conexion con = new Conexion();
        try {
            ServletOutputStream sos = response.getOutputStream();
            File reporte = new File(getServletConfig().getServletContext().getRealPath("/reportes/Usuarios.jasper"));
            byte[] bytes = JasperRunManager.runReportToPdf(reporte.getPath(), null, con.conecta());
            response.setContentType("application/pdf");
            response.setContentLength(bytes.length);
            sos.write(bytes, 0, bytes.length);
            sos.flush();
            sos.close();
        } catch (IOException | JRException ex) {
            Logger.getLogger(utileriasServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    
    private void verPDFUsuario(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        Conexion con = new Conexion();
        Map<String, Object> parametro = new HashMap<>();
        parametro.put("idUsu", Integer.parseInt(request.getParameter("clave")));
        try {
            ServletOutputStream sos = response.getOutputStream();
            File reporte = new File(getServletConfig().getServletContext().getRealPath("/reportes/Usuario.jasper"));
            byte[] bytes = JasperRunManager.runReportToPdf(reporte.getPath(), parametro, con.conecta());
            response.setContentType("application/pdf");
            response.setContentLength(bytes.length);
            sos.write(bytes, 0, bytes.length);
            sos.flush();
            sos.close();
        } catch (IOException | JRException ex) {
            Logger.getLogger(utileriasServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
