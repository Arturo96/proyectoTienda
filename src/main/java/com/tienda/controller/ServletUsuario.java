/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tienda.controller;

import com.tienda.jpa.ProductosJpaController;
import com.tienda.util.JPAFactory;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Carlos
 */
public class ServletUsuario extends HttpServlet {

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

        RequestDispatcher rd = null;
        HttpSession session = request.getSession();

        String rol = null;
        String email = null;
        String path = "index.jsp";
        String accion = request.getParameter("btnAccion");

        if (accion.equals("signIn")) {
            email = request.getParameter("txtEmail");
            String clave = request.getParameter("txtPassword");

            if (email.equals("admin@gmail.com") && clave.equals("admin")) {
                rol = "admin";
                
            }

            if (email.equals("empleado@gmail.com") && clave.equals("empleado")) {
                rol = "empleado";
                
            }
        } 
        
        ProductosJpaController productosJpaController = new
                ProductosJpaController(JPAFactory.getFACTORY());

        session.setAttribute("SESION", rol);
        session.setAttribute("USUARIO", email);
        session.setAttribute("PRODUCTOS", productosJpaController.findProductosEntities());
        request.getRequestDispatcher(path).forward(request, response);

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
