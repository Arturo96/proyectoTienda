/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tienda.controller;

import com.tienda.data.ProductoData;
import com.tienda.dto.ProductoDTO;
import com.tienda.jpa.FacturasJpaController;
import com.tienda.model.Facturas;
import com.tienda.util.Conexion;
import com.tienda.util.Correo;
import com.tienda.util.JPAFactory;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ServletReportePdto extends HttpServlet {

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
        String jasperfile = session.getServletContext().getRealPath("/ReportePdto.jasper");

        Integer idfactura = Integer.parseInt(request.getParameter("txtIdFactura"));
        String nombre_pdto = request.getParameter("txtNombrepdto");

        FacturasJpaController facturasJpaController = new FacturasJpaController(JPAFactory.getFACTORY());

        Facturas factura = facturasJpaController.findFacturas(idfactura);
        
        List<ProductoDTO> listadoPdtos = ProductoData.getListadoconGarantia();
        
        ProductoDTO producto = listadoPdtos.get(listadoPdtos.indexOf
        (new ProductoDTO(idfactura, nombre_pdto, "", 0.0, "", "", 0, "", "")));
        
        

        try {

            Map parameters = new HashMap();

            String nombreCompleto = factura.getCliente().getNombres() + " "
                    + factura.getCliente().getApellidos();

            parameters.put("DOC_CLIENTE", factura.getCliente().getNrodocumento());
            parameters.put("NOM_CLIENTE", nombreCompleto);
            parameters.put("FECHA_COMPRA", producto.getFechacompra());
            parameters.put("FECHA_GARANTIA", producto.getFechagarantia());

            byte[] fichero = JasperRunManager.runReportToPdf (jasperfile, parameters, Conexion.getConexion());

            ServletOutputStream out;
            
           

            // Y enviamos el pdf a la salida del navegador como podríamos hacer con cualquier otro pdf
            response.setContentType("application/pdf");
            response.setHeader("Content-disposition", "inline; filename=facturaPdto.pdf");
            response.setHeader("Cache-Control", "max-age=30");
            response.setHeader("Pragma", "No-cache");
            response.setDateHeader("Expires", 0);
            response.setContentLength(fichero.length);
            out = response.getOutputStream();

            out.write(fichero, 0, fichero.length);
            out.flush();
            out.close();

        } catch (JRException ex) {
            Logger.getLogger(ServletCompra.class.getName()).log(Level.SEVERE, null, ex);
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
