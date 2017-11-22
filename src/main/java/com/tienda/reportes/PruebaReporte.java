/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tienda.reportes;

import com.tienda.util.Conexion;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.view.JasperViewer;

public class PruebaReporte {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws JRException {
       
        // descarga dentro del mismo proyecto
        
        Map parameters = new HashMap();
        
        parameters.put("DOC_CLIENTE", "95312067662");
        
        
        
        JasperPrint jasperPrint = JasperFillManager.fillReport(
                "src/main/java/com/tienda/reportes/ReporteCompra.jasper", parameters,
                Conexion.getConexion());
        JRPdfExporter exp = new JRPdfExporter();
        exp.setExporterInput(new SimpleExporterInput(jasperPrint));
        exp.setExporterOutput(new SimpleOutputStreamExporterOutput("ReporteCompra.pdf"));
        SimplePdfExporterConfiguration conf = new SimplePdfExporterConfiguration();
        exp.setConfiguration(conf);
        exp.exportReport();
        
    }
    
}
