/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tienda.controller;

import com.tienda.data.Meses;
import com.tienda.data.ProductoData;
import com.tienda.dto.ProductoDTO;
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
import com.tienda.jpa.TipoproductosJpaController;
import com.tienda.model.Clientes;
import com.tienda.model.Detallefacturas;
import com.tienda.model.Facturas;
import com.tienda.model.Productos;
import com.tienda.model.Tipoproductos;
import com.tienda.util.JPAFactory;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

        if (cliente == null) {
            session.setAttribute("MSJCLIENTE", "Error: no está registrado un "
                    + "cliente con el documento " + documento);
        } else {
            path = "view/listarPdtosCliente.jsp";

            FacturasJpaController facturasJpaController
                    = new FacturasJpaController(JPAFactory.getFACTORY());

            List<Facturas> listadoFacturas
                    = facturasJpaController.findFacturasbyCliente(cliente);

            DetallefacturasJpaController detallefacturasJpaController
                    = new DetallefacturasJpaController(JPAFactory.getFACTORY());

            List<Detallefacturas> detallefacturas;

            Productos producto;

            List<ProductoDTO> listadoProductosconGarantia = new ArrayList<>();
            List<ProductoDTO> listadoProductossinGarantia = new ArrayList<>();
            ProductoDTO productoDTO;

            Date fecha_actual = new java.util.Date();
            String tipoproducto;

            for (Facturas factura : listadoFacturas) {
                Date fecha_factura = factura.getFechafactura();

                detallefacturas = detallefacturasJpaController.findDetallefacturasbyFactura(factura);
                for (Detallefacturas detallefact : detallefacturas) {
                    producto = detallefact.getProductos();
                    tipoproducto = producto.getTipopdto().getDescripciontipopdto();

                    Calendar calendar = Calendar.getInstance();

                    calendar.setTime(fecha_factura); // Configuramos la fecha que se recibe

                    calendar.add(Calendar.MONTH, producto.getTipopdto().getTiempogarantia()); // numero de días a añadir, o restar en caso de días<0

                    Date fecha_garantia = calendar.getTime();

                    int res = fecha_actual.compareTo(fecha_garantia);

                    String fecha_garantiaOrg = new SimpleDateFormat("dd-MM-yyyy").format(fecha_garantia);
                    String fecha_facturaOrg = new SimpleDateFormat("dd-MM-yyyy").format(fecha_factura);

                    fecha_garantiaOrg = Meses.fechaEstandar(fecha_garantiaOrg);
                    fecha_facturaOrg = Meses.fechaEstandar(fecha_facturaOrg);

                    productoDTO = new ProductoDTO(factura.getIdfactura(),
                            producto.getNombrepdto(), producto.getMarca(),
                            producto.getPrecioventa(), tipoproducto,
                            producto.getDistribuidorgarantia(),
                            detallefact.getCantidad(), fecha_facturaOrg, fecha_garantiaOrg);
                    if (res > 0) {
                        listadoProductossinGarantia.add(productoDTO);
                    } else {
                        listadoProductosconGarantia.add(productoDTO);
                    }

                }

            }
            
            ProductoData.setListadoconGarantia(listadoProductosconGarantia);
            
            
            session.setAttribute("PDTOSCONGARANTIA", listadoProductosconGarantia);
            session.setAttribute("PDTOSSINGARANTIA", listadoProductossinGarantia);
            session.setAttribute("CLIENTE", cliente);
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
