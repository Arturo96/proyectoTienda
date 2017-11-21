/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tienda.model;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author GABRIEL
 */
@Entity
@Table(name = "productos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Productos.findAll", query = "SELECT p FROM Productos p")
    , @NamedQuery(name = "Productos.findByIdpdto", query = "SELECT p FROM Productos p WHERE p.idpdto = :idpdto")
    , @NamedQuery(name = "Productos.findByNombrepdto", query = "SELECT p FROM Productos p WHERE p.nombrepdto = :nombrepdto")
    , @NamedQuery(name = "Productos.findByMarca", query = "SELECT p FROM Productos p WHERE p.marca = :marca")
    , @NamedQuery(name = "Productos.findByPrecioventa", query = "SELECT p FROM Productos p WHERE p.precioventa = :precioventa")
    , @NamedQuery(name = "Productos.findByDistribuidorgarantia", query = "SELECT p FROM Productos p WHERE p.distribuidorgarantia = :distribuidorgarantia")})
public class Productos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idpdto")
    private Integer idpdto;
    @Size(max = 2147483647)
    @Column(name = "nombrepdto")
    private String nombrepdto;
    @Size(max = 2147483647)
    @Column(name = "marca")
    private String marca;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "precioventa")
    private Double precioventa;
    @Size(max = 2147483647)
    @Column(name = "distribuidorgarantia")
    private String distribuidorgarantia;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productos")
    private List<Detallefacturas> detallefacturasList;
    @JoinColumn(name = "proveedor", referencedColumnName = "idprov")
    @ManyToOne
    private Proveedores proveedor;
    @JoinColumn(name = "tipopdto", referencedColumnName = "idtipopdto")
    @ManyToOne
    private Tipoproductos tipopdto;

    public Productos() {
    }

    public Productos(Integer idpdto) {
        this.idpdto = idpdto;
    }

    public Integer getIdpdto() {
        return idpdto;
    }

    public void setIdpdto(Integer idpdto) {
        this.idpdto = idpdto;
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

    public String getDistribuidorgarantia() {
        return distribuidorgarantia;
    }

    public void setDistribuidorgarantia(String distribuidorgarantia) {
        this.distribuidorgarantia = distribuidorgarantia;
    }

    @XmlTransient
    public List<Detallefacturas> getDetallefacturasList() {
        return detallefacturasList;
    }

    public void setDetallefacturasList(List<Detallefacturas> detallefacturasList) {
        this.detallefacturasList = detallefacturasList;
    }

    public Proveedores getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedores proveedor) {
        this.proveedor = proveedor;
    }

    public Tipoproductos getTipopdto() {
        return tipopdto;
    }

    public void setTipopdto(Tipoproductos tipopdto) {
        this.tipopdto = tipopdto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idpdto != null ? idpdto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Productos)) {
            return false;
        }
        Productos other = (Productos) object;
        if ((this.idpdto == null && other.idpdto != null) || (this.idpdto != null && !this.idpdto.equals(other.idpdto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tienda.model.Productos[ idpdto=" + idpdto + " ]";
    }
    
}
