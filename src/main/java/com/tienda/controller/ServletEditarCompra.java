/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tienda.controller;

import com.tienda.dto.PdtoConsecutivoDTO;
import com.tienda.jpa.DetallefacturasJpaController;
import com.tienda.jpa.FacturasJpaController;
import com.tienda.jpa.ProductosJpaController;
import com.tienda.jpa.exceptions.NonexistentEntityException;
import com.tienda.model.Detallefacturas;
import com.tienda.model.DetallefacturasPK;
import com.tienda.model.Facturas;
import com.tienda.model.Productos;
import com.tienda.util.JPAFactory;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
public class ServletEditarCompra extends HttpServlet {

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
        String path = "";

        Integer id_factura = Integer.parseInt(request.getParameter("txtIdFactura"));

        String accion = request.getParameter("btnaccion");

        DetallefacturasJpaController detallefacturasJpaController = new DetallefacturasJpaController(JPAFactory.getFACTORY());

        FacturasJpaController facturasJpaController = new FacturasJpaController(JPAFactory.getFACTORY());

        Facturas factura = facturasJpaController.findFacturas(id_factura);

        List<Detallefacturas> detallesFactura = detallefacturasJpaController.
                findDetallefacturasbyFactura(factura);

        switch (accion) {

            case "buscar":

                List<PdtoConsecutivoDTO> productos = new ArrayList<>();
                Integer i = 1;
                for (Detallefacturas detalle : detallesFactura) {
                    productos.add(new PdtoConsecutivoDTO(i,
                            detalle.getProductos().getNombrepdto(),
                            detalle.getCantidad()));
                    i++;
                }

                session.setAttribute("PDTOSFACTURA", productos);
                session.setAttribute("FACTURA", factura);
                session.setAttribute("CLIENTE", factura.getCliente());
                path = "view/editarCompra.jsp";
                break;
            case "editar":

                List<String> nombreProductos = new ArrayList<>();
                List<Integer> cantidadProductos = new ArrayList<>();
                int numProductos = detallesFactura.size();

                for (int j = 1; j <= numProductos; j++) {
                    String nombrePdtoAux = request.getParameter("txtNombrePdto" + j);
                    if (j > 1) {
                        for (int k = 1; k < j; k++) {
                            if (nombreProductos.get(k - 1).equals(nombrePdtoAux)) {
                                session.setAttribute("MENSAJEEDITAR", "No se puede ingresar más de un producto igual.");
                                request.getRequestDispatcher("view/editarCompra.jsp").forward(request, response);
                                return;
                            }
                        }
                    }
                    nombreProductos.add(nombrePdtoAux);
                    String cantidadPdtoAux = request.getParameter("txtCantidadPdto" + j);
                    if (!cantidadPdtoAux.matches("[1-9][0-9]*")) {
                        session.setAttribute("MENSAJEEDITAR", "Se debe ingresar un número en cantidad del producto comprado.");
                        request.getRequestDispatcher("view/editarCompra.jsp").forward(request, response);
                        return;
                    }
                    cantidadProductos.add(Integer.parseInt(cantidadPdtoAux));
                }

                ProductosJpaController productosJpaController = new ProductosJpaController(JPAFactory.getFACTORY());

                for (int j = 0; j < numProductos; j++) {
                    Productos producto
                            = productosJpaController.findProductosbyName(nombreProductos.get(j));
                    Detallefacturas detFactura = detallesFactura.get(j);
                    try {
                        detallefacturasJpaController.destroy(detallesFactura.get(j).getDetallefacturasPK());
                        DetallefacturasPK aux = new DetallefacturasPK(id_factura, producto.getIdpdto());
                        detFactura.setDetallefacturasPK(aux);
                        detFactura.setProductos(producto);
                        detFactura.setCantidad(cantidadProductos.get(j));
                        detallefacturasJpaController.create(detFactura);
                    } catch (NonexistentEntityException ex) {
                        Logger.getLogger(ServletEditarCompra.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (Exception ex) {
                        Logger.getLogger(ServletEditarCompra.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                path = "view/listarCompras.jsp";
                break;

        }

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
