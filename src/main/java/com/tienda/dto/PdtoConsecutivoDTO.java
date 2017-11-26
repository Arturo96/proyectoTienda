/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tienda.dto;

import java.util.Objects;


public class PdtoConsecutivoDTO {
    private Integer consecutivo;
    private String nombre;
    private Integer cantidad;

    public PdtoConsecutivoDTO(Integer consecutivo, String nombre, Integer cantidad) {
        this.consecutivo = consecutivo;
        this.nombre = nombre;
        this.cantidad = cantidad;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(Integer consecutivo) {
        this.consecutivo = consecutivo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.consecutivo);
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
        final PdtoConsecutivoDTO other = (PdtoConsecutivoDTO) obj;
        if (!Objects.equals(this.consecutivo, other.consecutivo)) {
            return false;
        }
        return true;
    }
    
    
    
}
