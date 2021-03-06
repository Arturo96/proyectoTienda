/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tienda.controller;

import com.tienda.jpa.ClientesJpaController;
import com.tienda.jpa.DetallefacturasJpaController;
import com.tienda.jpa.FacturasJpaController;
import com.tienda.jpa.ProductosJpaController;
import com.tienda.jpa.TipoidclientesJpaController;
import com.tienda.jpa.UsuariosJpaController;
import com.tienda.model.CarroCompra;
import com.tienda.model.Clientes;
import com.tienda.model.Detallefacturas;
import com.tienda.model.Facturas;
import com.tienda.model.Productos;
import com.tienda.model.Usuarios;
import com.tienda.util.Conexion;
import com.tienda.util.Correo;
import com.tienda.util.JPAFactory;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

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
        String jasperfile = session.getServletContext().getRealPath("/ReporteCompra.jasper");
        // Cliente
        String documento = request.getParameter("txtDocumento");
        int tipodocumento = Integer.parseInt(request.getParameter("txttipoDocumento"));
        String nombres = request.getParameter("txtNombres");
        String apellidos = request.getParameter("txtApellidos");
        String telefono = request.getParameter("txtTelefono");
        String direccion = request.getParameter("txtDireccion");
        String email = request.getParameter("txtEmail");
        Date fechacompra = Date.valueOf(request.getParameter("txtFechacompra"));

        ClientesJpaController clientesJpaController = new ClientesJpaController(JPAFactory.getFACTORY());

        Clientes cliente = clientesJpaController.findClientes(documento);

        if (cliente == null) {

            TipoidclientesJpaController tipoidclientesJpaController
                    = new TipoidclientesJpaController(JPAFactory.getFACTORY());

            cliente = new Clientes(documento);
            cliente.setTipodocumento(tipoidclientesJpaController.findTipoidclientes(tipodocumento));
            cliente.setNombres(nombres);
            cliente.setApellidos(apellidos);
            cliente.setTelefonocte(telefono);
            cliente.setDireccioncte(direccion);
            cliente.setEmailcte(email);
            try {
                clientesJpaController.create(cliente);
            } catch (Exception ex) {
                Logger.getLogger(ServletCompra.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // Productos
        String nombreproducto;
        int cantidadproducto;
        int numproductos = Integer.parseInt(request.getParameter("numProductos"));

        List<CarroCompra> carrito = new ArrayList<>();

        ProductosJpaController productosJpaController = new ProductosJpaController(JPAFactory.getFACTORY());

        Productos producto;

        double precioTotal = 0;

        for (int i = 1; i <= numproductos; i++) {
            nombreproducto = request.getParameter("txtNombrePdto" + i);
            if (i > 1) {
                for (int j = 1; j < i; j++) {
                    if (carrito.get(j - 1).getNombrePdto().equals(nombreproducto)) {
                        session.setAttribute("MENSAJE", "No se puede ingresar más de un producto igual.");
                        request.getRequestDispatcher("view/registrarCompra.jsp").forward(request, response);
                        return;
                    }
                }
            }
            String auxTxtCantidadPdto = request.getParameter("txtCantidadPdto" + i);
            if (!auxTxtCantidadPdto.matches("[1-9][0-9]*")) {
                session.setAttribute("MENSAJE", "Se debe ingresar un número en cantidad del producto comprado.");
                request.getRequestDispatcher("view/registrarCompra.jsp").forward(request, response);
                return;
            }
            cantidadproducto = Integer.parseInt(auxTxtCantidadPdto);
            producto = productosJpaController.findProductosbyName(nombreproducto);
            precioTotal += producto.getPrecioventa() * cantidadproducto;
            carrito.add(new CarroCompra(nombreproducto, cantidadproducto));
        }

        // Fin Productos
        String email_usuario = request.getParameter("txtUsuario");

        // Add Factura
        FacturasJpaController facturasJpaController
                = new FacturasJpaController(JPAFactory.getFACTORY());

        List<Facturas> listado_facturas = facturasJpaController.findFacturasEntities();

        Integer id_factura = 1;

        if (listado_facturas != null) {
            id_factura = listado_facturas
                    .get(listado_facturas.size() - 1).getIdfactura() + 1;
        }

        Facturas factura = new Facturas(id_factura);
        factura.setCliente(cliente);
        factura.setValorfactura(precioTotal);
        factura.setFechafactura(fechacompra);

        UsuariosJpaController usuariosJpaController
                = new UsuariosJpaController(JPAFactory.getFACTORY());

        Usuarios usuario = usuariosJpaController.findUsuarios(email_usuario);

        factura.setUsuario(usuario);
        try {
            facturasJpaController.create(factura);
        } catch (Exception ex) {
            Logger.getLogger(ServletCompra.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Detalle factura
        DetallefacturasJpaController detallefacturasJpaController
                = new DetallefacturasJpaController(JPAFactory.getFACTORY());

        Detallefacturas detalle;

        for (CarroCompra detallepdto : carrito) {
            producto = productosJpaController.findProductosbyName(detallepdto.getNombrePdto());
            detalle = new Detallefacturas(id_factura, producto.getIdpdto());
//             detalle = new Detallefacturas();
            detalle.setFacturas(factura);
            detalle.setProductos(producto);
            detalle.setCantidad(detallepdto.getCantidad());
            try {
                detallefacturasJpaController.create(detalle);
            } catch (Exception ex) {
                Logger.getLogger(ServletCompra.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // Crear Reporte
        try {

            Map parameters = new HashMap();

            String nombreCompleto = nombres + " " + apellidos;

            parameters.put("DOC_CLIENTE", documento);
            parameters.put("NOM_CLIENTE", nombreCompleto);
            parameters.put("PAGO_TOTAL", precioTotal);
            parameters.put("IDFACTURA", id_factura);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperfile,
                    parameters,
                    Conexion.getConexion());
            String rutaPdf = "D:\\ProyectosJava\\proyectoTienda\\ReporteCompra.pdf";
            JRPdfExporter exp = new JRPdfExporter();
            exp.setExporterInput(new SimpleExporterInput(jasperPrint));
            exp.setExporterOutput(new SimpleOutputStreamExporterOutput(rutaPdf));
            SimplePdfExporterConfiguration conf = new SimplePdfExporterConfiguration();
            exp.setConfiguration(conf);
            exp.exportReport();

            // Notificación via mail
            Correo.mandarCorreo(email, rutaPdf);

            //
        } catch (JRException ex) {
            Logger.getLogger(ServletCompra.class.getName()).log(Level.SEVERE, null, ex);
        }

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
