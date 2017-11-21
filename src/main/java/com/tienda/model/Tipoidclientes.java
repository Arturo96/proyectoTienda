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
@Table(name = "tipoidclientes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tipoidclientes.findAll", query = "SELECT t FROM Tipoidclientes t")
    , @NamedQuery(name = "Tipoidclientes.findByIdtipodocumento", query = "SELECT t FROM Tipoidclientes t WHERE t.idtipodocumento = :idtipodocumento")
    , @NamedQuery(name = "Tipoidclientes.findByDescripciontipodocumento", query = "SELECT t FROM Tipoidclientes t WHERE t.descripciontipodocumento = :descripciontipodocumento")})
public class Tipoidclientes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idtipodocumento")
    private Integer idtipodocumento;
    @Size(max = 2147483647)
    @Column(name = "descripciontipodocumento")
    private String descripciontipodocumento;
    @OneToMany(mappedBy = "tipodocumento")
    private List<Clientes> clientesList;

    public Tipoidclientes() {
    }

    public Tipoidclientes(Integer idtipodocumento) {
        this.idtipodocumento = idtipodocumento;
    }

    public Integer getIdtipodocumento() {
        return idtipodocumento;
    }

    public void setIdtipodocumento(Integer idtipodocumento) {
        this.idtipodocumento = idtipodocumento;
    }

    public String getDescripciontipodocumento() {
        return descripciontipodocumento;
    }

    public void setDescripciontipodocumento(String descripciontipodocumento) {
        this.descripciontipodocumento = descripciontipodocumento;
    }

    @XmlTransient
    public List<Clientes> getClientesList() {
        return clientesList;
    }

    public void setClientesList(List<Clientes> clientesList) {
        this.clientesList = clientesList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idtipodocumento != null ? idtipodocumento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tipoidclientes)) {
            return false;
        }
        Tipoidclientes other = (Tipoidclientes) object;
        if ((this.idtipodocumento == null && other.idtipodocumento != null) || (this.idtipodocumento != null && !this.idtipodocumento.equals(other.idtipodocumento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tienda.model.Tipoidclientes[ idtipodocumento=" + idtipodocumento + " ]";
    }
    
}
