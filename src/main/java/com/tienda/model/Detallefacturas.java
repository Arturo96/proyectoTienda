/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tienda.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author GABRIEL
 */
@Entity
@Table(name = "detallefacturas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Detallefacturas.findAll", query = "SELECT d FROM Detallefacturas d")
    , @NamedQuery(name = "Detallefacturas.findByFactura", query = "SELECT d FROM Detallefacturas d WHERE d.detallefacturasPK.factura = :factura")
    , @NamedQuery(name = "Detallefacturas.findByProducto", query = "SELECT d FROM Detallefacturas d WHERE d.detallefacturasPK.producto = :producto")
    , @NamedQuery(name = "Detallefacturas.findByCantidad", query = "SELECT d FROM Detallefacturas d WHERE d.cantidad = :cantidad")})
public class Detallefacturas implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DetallefacturasPK detallefacturasPK;
    @Column(name = "cantidad")
    private Integer cantidad;
    @JoinColumn(name = "factura", referencedColumnName = "idfactura", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Facturas facturas;
    @JoinColumn(name = "producto", referencedColumnName = "idpdto", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Productos productos;

    public Detallefacturas() {
    }

    public Detallefacturas(DetallefacturasPK detallefacturasPK) {
        this.detallefacturasPK = detallefacturasPK;
    }

    public Detallefacturas(int factura, int producto) {
        this.detallefacturasPK = new DetallefacturasPK(factura, producto);
    }

    public DetallefacturasPK getDetallefacturasPK() {
        return detallefacturasPK;
    }

    public void setDetallefacturasPK(DetallefacturasPK detallefacturasPK) {
        this.detallefacturasPK = detallefacturasPK;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Facturas getFacturas() {
        return facturas;
    }

    public void setFacturas(Facturas facturas) {
        this.facturas = facturas;
    }

    public Productos getProductos() {
        return productos;
    }

    public void setProductos(Productos productos) {
        this.productos = productos;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (detallefacturasPK != null ? detallefacturasPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Detallefacturas)) {
            return false;
        }
        Detallefacturas other = (Detallefacturas) object;
        if ((this.detallefacturasPK == null && other.detallefacturasPK != null) || (this.detallefacturasPK != null && !this.detallefacturasPK.equals(other.detallefacturasPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tienda.model.Detallefacturas[ detallefacturasPK=" + detallefacturasPK + " ]";
    }
    
}
