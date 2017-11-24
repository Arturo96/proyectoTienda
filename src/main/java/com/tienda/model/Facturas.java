/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tienda.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author GABRIEL
 */
@Entity
@Table(name = "facturas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Facturas.findAll", query = "SELECT f FROM Facturas f")
    , @NamedQuery(name = "Facturas.findByIdfactura", query = "SELECT f FROM Facturas f WHERE f.idfactura = :idfactura")
    , @NamedQuery(name = "Facturas.findByValorfactura", query = "SELECT f FROM Facturas f WHERE f.valorfactura = :valorfactura")
    , @NamedQuery(name = "Facturas.findByCliente", query = "SELECT f FROM Facturas f WHERE f.cliente = :cliente ORDER BY f.fechafactura DESC")    
    , @NamedQuery(name = "Facturas.findByFechafactura", query = "SELECT f FROM Facturas f WHERE f.fechafactura = :fechafactura")})
public class Facturas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idfactura")
    private Integer idfactura;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valorfactura")
    private Double valorfactura;
    @Column(name = "fechafactura")
    @Temporal(TemporalType.DATE)
    private Date fechafactura;
    @JoinColumn(name = "cliente", referencedColumnName = "nrodocumento")
    @ManyToOne
    private Clientes cliente;
    @JoinColumn(name = "usuario", referencedColumnName = "emailusuario")
    @ManyToOne
    private Usuarios usuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "facturas")
    private List<Detallefacturas> detallefacturasList;

    public Facturas() {
    }

    public Facturas(Integer idfactura) {
        this.idfactura = idfactura;
    }

    public Integer getIdfactura() {
        return idfactura;
    }

    public void setIdfactura(Integer idfactura) {
        this.idfactura = idfactura;
    }

    public Double getValorfactura() {
        return valorfactura;
    }

    public void setValorfactura(Double valorfactura) {
        this.valorfactura = valorfactura;
    }

    public Date getFechafactura() {
        return fechafactura;
    }

    public void setFechafactura(Date fechafactura) {
        this.fechafactura = fechafactura;
    }

    public Clientes getCliente() {
        return cliente;
    }

    public void setCliente(Clientes cliente) {
        this.cliente = cliente;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    @XmlTransient
    public List<Detallefacturas> getDetallefacturasList() {
        return detallefacturasList;
    }

    public void setDetallefacturasList(List<Detallefacturas> detallefacturasList) {
        this.detallefacturasList = detallefacturasList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idfactura != null ? idfactura.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Facturas)) {
            return false;
        }
        Facturas other = (Facturas) object;
        if ((this.idfactura == null && other.idfactura != null) || (this.idfactura != null && !this.idfactura.equals(other.idfactura))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tienda.model.Facturas[ idfactura=" + idfactura + " ]";
    }
    
}
