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
@Table(name = "clientes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Clientes.findAll", query = "SELECT c FROM Clientes c")
    , @NamedQuery(name = "Clientes.findByNrodocumento", query = "SELECT c FROM Clientes c WHERE c.nrodocumento = :nrodocumento")
    , @NamedQuery(name = "Clientes.findByNombres", query = "SELECT c FROM Clientes c WHERE c.nombres = :nombres")
    , @NamedQuery(name = "Clientes.findByApellidos", query = "SELECT c FROM Clientes c WHERE c.apellidos = :apellidos")
    , @NamedQuery(name = "Clientes.findByTelefonocte", query = "SELECT c FROM Clientes c WHERE c.telefonocte = :telefonocte")
    , @NamedQuery(name = "Clientes.findByDireccioncte", query = "SELECT c FROM Clientes c WHERE c.direccioncte = :direccioncte")
    , @NamedQuery(name = "Clientes.findByEmailcte", query = "SELECT c FROM Clientes c WHERE c.emailcte = :emailcte")})
public class Clientes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "nrodocumento")
    private String nrodocumento;
    @Size(max = 2147483647)
    @Column(name = "nombres")
    private String nombres;
    @Size(max = 2147483647)
    @Column(name = "apellidos")
    private String apellidos;
    @Size(max = 2147483647)
    @Column(name = "telefonocte")
    private String telefonocte;
    @Size(max = 2147483647)
    @Column(name = "direccioncte")
    private String direccioncte;
    @Size(max = 2147483647)
    @Column(name = "emailcte")
    private String emailcte;
    @OneToMany(mappedBy = "cliente")
    private List<Facturas> facturasList;
    @JoinColumn(name = "tipodocumento", referencedColumnName = "idtipodocumento")
    @ManyToOne
    private Tipoidclientes tipodocumento;

    public Clientes() {
    }

    public Clientes(String nrodocumento) {
        this.nrodocumento = nrodocumento;
    }

    public String getNrodocumento() {
        return nrodocumento;
    }

    public void setNrodocumento(String nrodocumento) {
        this.nrodocumento = nrodocumento;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefonocte() {
        return telefonocte;
    }

    public void setTelefonocte(String telefonocte) {
        this.telefonocte = telefonocte;
    }

    public String getDireccioncte() {
        return direccioncte;
    }

    public void setDireccioncte(String direccioncte) {
        this.direccioncte = direccioncte;
    }

    public String getEmailcte() {
        return emailcte;
    }

    public void setEmailcte(String emailcte) {
        this.emailcte = emailcte;
    }

    @XmlTransient
    public List<Facturas> getFacturasList() {
        return facturasList;
    }

    public void setFacturasList(List<Facturas> facturasList) {
        this.facturasList = facturasList;
    }

    public Tipoidclientes getTipodocumento() {
        return tipodocumento;
    }

    public void setTipodocumento(Tipoidclientes tipodocumento) {
        this.tipodocumento = tipodocumento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nrodocumento != null ? nrodocumento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Clientes)) {
            return false;
        }
        Clientes other = (Clientes) object;
        if ((this.nrodocumento == null && other.nrodocumento != null) || (this.nrodocumento != null && !this.nrodocumento.equals(other.nrodocumento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tienda.model.Clientes[ nrodocumento=" + nrodocumento + " ]";
    }
    
}
