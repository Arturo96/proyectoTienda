/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tienda.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author GABRIEL
 */
@Entity
@Table(name = "tipoproductos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tipoproductos.findAll", query = "SELECT t FROM Tipoproductos t")
    , @NamedQuery(name = "Tipoproductos.findByIdtipopdto", query = "SELECT t FROM Tipoproductos t WHERE t.idtipopdto = :idtipopdto")
    , @NamedQuery(name = "Tipoproductos.findByDescripciontipopdto", query = "SELECT t FROM Tipoproductos t WHERE t.descripciontipopdto = :descripciontipopdto")
    , @NamedQuery(name = "Tipoproductos.findByTiempogarantia", query = "SELECT t FROM Tipoproductos t WHERE t.tiempogarantia = :tiempogarantia")})
public class Tipoproductos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idtipopdto")
    private Integer idtipopdto;
    @Size(max = 2147483647)
    @Column(name = "descripciontipopdto")
    private String descripciontipopdto;
    @Column(name = "tiempogarantia")
    private Integer tiempogarantia;
    @OneToMany(mappedBy = "tipopdto")
    private List<Productos> productosList;

    public Tipoproductos() {
    }

    public Tipoproductos(Integer idtipopdto) {
        this.idtipopdto = idtipopdto;
    }

    public Integer getIdtipopdto() {
        return idtipopdto;
    }

    public void setIdtipopdto(Integer idtipopdto) {
        this.idtipopdto = idtipopdto;
    }

    public String getDescripciontipopdto() {
        return descripciontipopdto;
    }

    public void setDescripciontipopdto(String descripciontipopdto) {
        this.descripciontipopdto = descripciontipopdto;
    }

    public Integer getTiempogarantia() {
        return tiempogarantia;
    }

    public void setTiempogarantia(Integer tiempogarantia) {
        this.tiempogarantia = tiempogarantia;
    }

    @XmlTransient
    public List<Productos> getProductosList() {
        return productosList;
    }

    public void setProductosList(List<Productos> productosList) {
        this.productosList = productosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idtipopdto != null ? idtipopdto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tipoproductos)) {
            return false;
        }
        Tipoproductos other = (Tipoproductos) object;
        if ((this.idtipopdto == null && other.idtipopdto != null) || (this.idtipopdto != null && !this.idtipopdto.equals(other.idtipopdto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tienda.model.Tipoproductos[ idtipopdto=" + idtipopdto + " ]";
    }
    
}
