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
@Table(name = "usuarios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuarios.findAll", query = "SELECT u FROM Usuarios u")
    , @NamedQuery(name = "Usuarios.findByEmailusuario", query = "SELECT u FROM Usuarios u WHERE u.emailusuario = :emailusuario")
    , @NamedQuery(name = "Usuarios.findByPassword", query = "SELECT u FROM Usuarios u WHERE u.password = :password")})
public class Usuarios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "emailusuario")
    private String emailusuario;
    @Size(max = 2147483647)
    @Column(name = "password")
    private String password;
    @OneToMany(mappedBy = "usuario")
    private List<Facturas> facturasList;
    @JoinColumn(name = "rol", referencedColumnName = "idrol")
    @ManyToOne
    private Roles rol;

    public Usuarios() {
    }

    public Usuarios(String emailusuario) {
        this.emailusuario = emailusuario;
    }

    public String getEmailusuario() {
        return emailusuario;
    }

    public void setEmailusuario(String emailusuario) {
        this.emailusuario = emailusuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @XmlTransient
    public List<Facturas> getFacturasList() {
        return facturasList;
    }

    public void setFacturasList(List<Facturas> facturasList) {
        this.facturasList = facturasList;
    }

    public Roles getRol() {
        return rol;
    }

    public void setRol(Roles rol) {
        this.rol = rol;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (emailusuario != null ? emailusuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuarios)) {
            return false;
        }
        Usuarios other = (Usuarios) object;
        if ((this.emailusuario == null && other.emailusuario != null) || (this.emailusuario != null && !this.emailusuario.equals(other.emailusuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tienda.model.Usuarios[ emailusuario=" + emailusuario + " ]";
    }
    
}
