/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tienda.data;

import com.tienda.dto.ProductoDTO;
import java.util.List;


public class ProductoData {
    
    private static List<ProductoDTO> listadoconGarantia;
    

    public static List<ProductoDTO> getListadoconGarantia() {
        return listadoconGarantia;
    }

    public static void setListadoconGarantia(List<ProductoDTO> aListadoconGarantia) {
        listadoconGarantia = aListadoconGarantia;
    }

    
    
    
    
}
