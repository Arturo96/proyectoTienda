/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tienda.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.tienda.jpa.ClientesJpaController;
import com.tienda.jpa.DetallefacturasJpaController;
import com.tienda.jpa.FacturasJpaController;
import com.tienda.jpa.ProductosJpaController;
import com.tienda.model.Clientes;
import com.tienda.model.Detallefacturas;
import com.tienda.model.Facturas;
import com.tienda.util.JPAFactory;
import java.util.List;

/**
 *
 * @author Carlos
 */
public class ServletPdtosCliente extends HttpServlet {

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
        String path = "view/consultaProductos.jsp";
        
        
        ClientesJpaController clientesJpaController = new ClientesJpaController(JPAFactory.getFACTORY());

        Clientes cliente = clientesJpaController.findClientes(documento);
        
        if(cliente == null) {
            session.setAttribute("MSJCLIENTE", "Error: no está registrado un"
                    + "cliente con el documento " + documento);
        } else {
            path = "view/listarPdtosCliente.jsp";
            
            FacturasJpaController facturasJpaController
                = new FacturasJpaController(JPAFactory.getFACTORY());
            
            List<Facturas> listadoFacturas = 
                    facturasJpaController.findFacturasbyCliente(cliente);
            
            
            DetallefacturasJpaController detallefacturasJpaController =
                    new DetallefacturasJpaController(JPAFactory.getFACTORY());
            
            List<Detallefacturas> detallefactura;
            
            for (Facturas factura : listadoFacturas) {
                detallefactura = detallefacturasJpaController.findDetallefacturasbyFactura(factura);
            }
            
        }
        
        session.setAttribute("CLIENTE", cliente);
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
