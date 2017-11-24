
package com.tienda.dto;

import java.util.Date;
import java.util.Objects;


public class ProductoDTO {
    
    private Integer idfactura;
    private String nombrepdto;
    private String marca;    
    private Double precioventa;
    private String tipoproducto;
    private String distribuidorgarantia;
    private Integer cantidad;
    private Date fechacompra;

    public ProductoDTO(Integer idfactura, String nombrepdto, String marca, Double precioventa, String tipoproducto, String distribuidorgarantia, Integer cantidad, Date fechacompra) {
        this.idfactura = idfactura;
        this.nombrepdto = nombrepdto;
        this.marca = marca;
        this.precioventa = precioventa;
        this.tipoproducto = tipoproducto;
        this.distribuidorgarantia = distribuidorgarantia;
        this.cantidad = cantidad;
        this.fechacompra = fechacompra;
    }

    public ProductoDTO() {
    }

    public Integer getIdfactura() {
        return idfactura;
    }

    public void setIdfactura(Integer idfactura) {
        this.idfactura = idfactura;
    }

    public String getNombrepdto() {
        return nombrepdto;
    }

    public void setNombrepdto(String nombrepdto) {
        this.nombrepdto = nombrepdto;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Double getPrecioventa() {
        return precioventa;
    }

    public void setPrecioventa(Double precioventa) {
        this.precioventa = precioventa;
    }

    public String getTipoproducto() {
        return tipoproducto;
    }

    public void setTipoproducto(String tipoproducto) {
        this.tipoproducto = tipoproducto;
    }

    public String getDistribuidorgarantia() {
        return distribuidorgarantia;
    }

    public void setDistribuidorgarantia(String distribuidorgarantia) {
        this.distribuidorgarantia = distribuidorgarantia;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Date getFechacompra() {
        return fechacompra;
    }

    public void setFechacompra(Date fechacompra) {
        this.fechacompra = fechacompra;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.idfactura);
        hash = 41 * hash + Objects.hashCode(this.nombrepdto);
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
        final ProductoDTO other = (ProductoDTO) obj;
        if (!Objects.equals(this.nombrepdto, other.nombrepdto)) {
            return false;
        }
        if (!Objects.equals(this.idfactura, other.idfactura)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ProductoDTO{" + "idfactura=" + idfactura + ", nombrepdto=" + nombrepdto + ", marca=" + marca + ", precioventa=" + precioventa + ", tipoproducto=" + tipoproducto + ", distribuidorgarantia=" + distribuidorgarantia + ", cantidad=" + cantidad + ", fechacompra=" + fechacompra + '}';
    }
    
    
}
