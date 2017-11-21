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
@Table(name = "proveedores")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Proveedores.findAll", query = "SELECT p FROM Proveedores p")
    , @NamedQuery(name = "Proveedores.findByIdprov", query = "SELECT p FROM Proveedores p WHERE p.idprov = :idprov")
    , @NamedQuery(name = "Proveedores.findByNombreprov", query = "SELECT p FROM Proveedores p WHERE p.nombreprov = :nombreprov")
    , @NamedQuery(name = "Proveedores.findByTelefonoprov", query = "SELECT p FROM Proveedores p WHERE p.telefonoprov = :telefonoprov")
    , @NamedQuery(name = "Proveedores.findByDireccionprov", query = "SELECT p FROM Proveedores p WHERE p.direccionprov = :direccionprov")
    , @NamedQuery(name = "Proveedores.findByEmailprov", query = "SELECT p FROM Proveedores p WHERE p.emailprov = :emailprov")})
public class Proveedores implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idprov")
    private Integer idprov;
    @Size(max = 2147483647)
    @Column(name = "nombreprov")
    private String nombreprov;
    @Size(max = 2147483647)
    @Column(name = "telefonoprov")
    private String telefonoprov;
    @Size(max = 2147483647)
    @Column(name = "direccionprov")
    private String direccionprov;
    @Size(max = 2147483647)
    @Column(name = "emailprov")
    private String emailprov;
    @OneToMany(mappedBy = "proveedor")
    private List<Productos> productosList;

    public Proveedores() {
    }

    public Proveedores(Integer idprov) {
        this.idprov = idprov;
    }

    public Integer getIdprov() {
        return idprov;
    }

    public void setIdprov(Integer idprov) {
        this.idprov = idprov;
    }

    public String getNombreprov() {
        return nombreprov;
    }

    public void setNombreprov(String nombreprov) {
        this.nombreprov = nombreprov;
    }

    public String getTelefonoprov() {
        return telefonoprov;
    }

    public void setTelefonoprov(String telefonoprov) {
        this.telefonoprov = telefonoprov;
    }

    public String getDireccionprov() {
        return direccionprov;
    }

    public void setDireccionprov(String direccionprov) {
        this.direccionprov = direccionprov;
    }

    public String getEmailprov() {
        return emailprov;
    }

    public void setEmailprov(String emailprov) {
        this.emailprov = emailprov;
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
        hash += (idprov != null ? idprov.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Proveedores)) {
            return false;
        }
        Proveedores other = (Proveedores) object;
        if ((this.idprov == null && other.idprov != null) || (this.idprov != null && !this.idprov.equals(other.idprov))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tienda.model.Proveedores[ idprov=" + idprov + " ]";
    }
    
}
