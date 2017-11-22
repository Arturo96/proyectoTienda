/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tienda.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Conexion {
    
    private static Connection connection;
    
    public static Connection getConexion(){
        if(connection==null){
            String driver = "org.postgresql.Driver";
            String url = "jdbc:postgresql://localhost:5432/bdtienda";
            String usuario = "tienda";
            String clave = "admin";
            try {
                Class.forName(driver);
                connection = DriverManager.getConnection(url, usuario, clave);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return connection;        
    }
    public static void cerrarConexion(){
        if(connection!=null){
            try {                
                connection.close();
                connection = null;
            } catch (SQLException ex) {
                Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
    
}
