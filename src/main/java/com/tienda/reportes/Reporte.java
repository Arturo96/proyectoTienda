package com.tienda.reportes;

import com.tienda.util.Conexion;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.view.JasperViewer;

public class Reporte {

    public static void crearReporte(String documento, HttpSession session) throws JRException {

        // descarga dentro del mismo proyecto
        Map parameters = new HashMap();

        parameters.put("DOC_CLIENTE", documento);

        JasperPrint jasperPrint = JasperFillManager.fillReport(session.getServletContext()
                .getRealPath("ReporteCompra.jasper"),
                 parameters,
                Conexion.getConexion());
        JRPdfExporter exp = new JRPdfExporter();
        exp.setExporterInput(new SimpleExporterInput(jasperPrint));
        exp.setExporterOutput(new SimpleOutputStreamExporterOutput("ReporteCompra.pdf"));
        SimplePdfExporterConfiguration conf = new SimplePdfExporterConfiguration();
        exp.setConfiguration(conf);
        exp.exportReport();

    }

}
