/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tienda.controller;

import com.tienda.jpa.ProductosJpaController;
import com.tienda.jpa.ProveedoresJpaController;
import com.tienda.jpa.exceptions.NonexistentEntityException;
import com.tienda.model.Productos;
import com.tienda.model.Proveedores;
import com.tienda.util.JPAFactory;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Carlos
 */
public class ServletProveedor extends HttpServlet {

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

        String nombreProv, telefono, direccion, email;

        String path = "view/listarProveedores.jsp";
        Integer codigoProv = Integer.parseInt(request.getParameter("txtCodigoprov"));
        String accion = request.getParameter("btnaccion");

        ProveedoresJpaController proveedoresJpaController = new ProveedoresJpaController(JPAFactory.getFACTORY());

        Proveedores proveedor = proveedoresJpaController.findProveedores(codigoProv);

        switch (accion) {

            case "ingresar":
                nombreProv = request.getParameter("txtNombreprov");
                telefono = request.getParameter("txtTelefonoprov");
                direccion = request.getParameter("txtDireccionprov");
                email = request.getParameter("txtEmailprov");

                if (proveedor != null) {
                    session.setAttribute("MSJPROV", "Error: el proveedor ya existe.");
                } else {
                    proveedor = new Proveedores(codigoProv);
                    proveedor.setNombreprov(nombreProv);
                    proveedor.setTelefonoprov(telefono);
                    proveedor.setDireccionprov(direccion);
                    proveedor.setEmailprov(email);
                    try {
                        proveedoresJpaController.create(proveedor);
                        session.setAttribute("MSJPROV", "Proveedor ingresado correctamente.");

                    } catch (Exception ex) {
                        Logger.getLogger(ServletProveedor.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
                path = "view/crearProveedor.jsp";
                break;
            case "buscar":
                session.setAttribute("PROVEEDOR", proveedor);
                path = "view/editarProveedor.jsp";
                break;
            case "editar":
                nombreProv = request.getParameter("txtNombreprov");
                telefono = request.getParameter("txtTelefonoprov");
                direccion = request.getParameter("txtDireccionprov");
                email = request.getParameter("txtEmailprov");
                proveedor.setNombreprov(nombreProv);
                proveedor.setTelefonoprov(telefono);
                proveedor.setDireccionprov(direccion);
                proveedor.setEmailprov(email);

                 {
                    try {
                        proveedoresJpaController.edit(proveedor);
                    } catch (NonexistentEntityException ex) {
                        Logger.getLogger(ServletProveedor.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (Exception ex) {
                        Logger.getLogger(ServletProveedor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                break;
            case "eliminar":

                /* Validación FK */
                ProductosJpaController productosJpaController = new ProductosJpaController(JPAFactory.getFACTORY());

                List<Productos> listado = productosJpaController.findProductosEntities();

                boolean existeProvenProd = false;

                for (Productos producto : listado) {
                    if (producto.getProveedor().equals(proveedor)) {
                        existeProvenProd = true;
                        break;
                    }
                }

                if (existeProvenProd) {
                    session.setAttribute("MSJLISTAPROV", "Error: hay movimientos que dependen"
                            + " de ese proveedor");
                } else {
                    try {
                        proveedoresJpaController.destroy(codigoProv);
                    } catch (NonexistentEntityException ex) {
                        Logger.getLogger(ServletProveedor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                break;

        }

        session.setAttribute("PROVEEDORES", proveedoresJpaController.findProveedoresEntities());
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
