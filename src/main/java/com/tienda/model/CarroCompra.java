/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tienda.model;

import java.util.Objects;

/**
 *
 * @author Carlos
 */
public class CarroCompra {
    private String nombrePdto;
    private int cantidad;

    public CarroCompra(String nombrePdto, int cantidad) {
        this.nombrePdto = nombrePdto;
        this.cantidad = cantidad;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getNombrePdto() {
        return nombrePdto;
    }

    public void setNombrePdto(String nombrePdto) {
        this.nombrePdto = nombrePdto;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.nombrePdto);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CarroCompra other = (CarroCompra) obj;
        if (!Objects.equals(this.nombrePdto, other.nombrePdto)) {
            return false;
        }
        return true;
    }
    
    
    
}
