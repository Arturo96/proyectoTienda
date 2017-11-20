/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tienda.controller;

import com.tienda.model.CarroCompra;
import com.tienda.util.Correo;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Carlos
 */
public class ServletCompra extends HttpServlet {

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
            throws ServletException, IOException {
        
        
        HttpSession session = request.getSession();
        
        String documento = request.getParameter("txtDocumento");
        int tipodocumento = Integer.parseInt(request.getParameter("txttipoDocumento"));
        String nombres = request.getParameter("txtNombres");
        String apellidos = request.getParameter("txtApellidos");
        String telefono = request.getParameter("txtTelefono");
        String direccion = request.getParameter("txtDireccion");
        String email = request.getParameter("txtEmail");
        
        // Productos
        
        String nombreproducto;
        int cantidadproducto;
        int numproductos = Integer.parseInt(request.getParameter("numProductos"));
        
        List<CarroCompra> carrito = new ArrayList<>();
        
        for (int i = 1; i <= numproductos; i++) {
            nombreproducto = request.getParameter("txtNombrePdto" + i);
            cantidadproducto = Integer.parseInt(request.getParameter("txtCantidadPdto" + i));
            carrito.add(new CarroCompra(nombreproducto,cantidadproducto));
        }
        
        // Fin Productos
        
        String fechacompra = request.getParameter("txtFechacompra");
        
        String usuario = request.getParameter("txtUsuario");
        
        
        // Notificación via mail
        Correo.mandarCorreo(email);
        
        //
        
        
        session.setAttribute("MENSAJE", "Compra realizada con éxito.");
        request.getRequestDispatcher("view/registrarCompra.jsp").forward(request, response);
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
        processRequest(request, response);
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
        processRequest(request, response);
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

}
