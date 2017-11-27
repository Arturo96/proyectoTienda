/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tienda.jpa;

import com.tienda.jpa.exceptions.NonexistentEntityException;
import com.tienda.jpa.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.tienda.model.Roles;
import com.tienda.model.Facturas;
import com.tienda.model.Usuarios;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

/**
 *
 * @author GABRIEL
 */
public class UsuariosJpaController implements Serializable {

    public UsuariosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuarios usuarios) throws PreexistingEntityException, Exception {
        if (usuarios.getFacturasList() == null) {
            usuarios.setFacturasList(new ArrayList<Facturas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Roles rol = usuarios.getRol();
            if (rol != null) {
                rol = em.getReference(rol.getClass(), rol.getIdrol());
                usuarios.setRol(rol);
            }
            List<Facturas> attachedFacturasList = new ArrayList<Facturas>();
            for (Facturas facturasListFacturasToAttach : usuarios.getFacturasList()) {
                facturasListFacturasToAttach = em.getReference(facturasListFacturasToAttach.getClass(), facturasListFacturasToAttach.getIdfactura());
                attachedFacturasList.add(facturasListFacturasToAttach);
            }
            usuarios.setFacturasList(attachedFacturasList);
            em.persist(usuarios);
            if (rol != null) {
                rol.getUsuariosList().add(usuarios);
                rol = em.merge(rol);
            }
            for (Facturas facturasListFacturas : usuarios.getFacturasList()) {
                Usuarios oldUsuarioOfFacturasListFacturas = facturasListFacturas.getUsuario();
                facturasListFacturas.setUsuario(usuarios);
                facturasListFacturas = em.merge(facturasListFacturas);
                if (oldUsuarioOfFacturasListFacturas != null) {
                    oldUsuarioOfFacturasListFacturas.getFacturasList().remove(facturasListFacturas);
                    oldUsuarioOfFacturasListFacturas = em.merge(oldUsuarioOfFacturasListFacturas);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuarios(usuarios.getEmailusuario()) != null) {
                throw new PreexistingEntityException("Usuarios " + usuarios + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuarios usuarios) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuarios persistentUsuarios = em.find(Usuarios.class, usuarios.getEmailusuario());
            Roles rolOld = persistentUsuarios.getRol();
            Roles rolNew = usuarios.getRol();
            List<Facturas> facturasListOld = persistentUsuarios.getFacturasList();
            List<Facturas> facturasListNew = usuarios.getFacturasList();
            if (rolNew != null) {
                rolNew = em.getReference(rolNew.getClass(), rolNew.getIdrol());
                usuarios.setRol(rolNew);
            }
            List<Facturas> attachedFacturasListNew = new ArrayList<Facturas>();
            for (Facturas facturasListNewFacturasToAttach : facturasListNew) {
                facturasListNewFacturasToAttach = em.getReference(facturasListNewFacturasToAttach.getClass(), facturasListNewFacturasToAttach.getIdfactura());
                attachedFacturasListNew.add(facturasListNewFacturasToAttach);
            }
            facturasListNew = attachedFacturasListNew;
            usuarios.setFacturasList(facturasListNew);
            usuarios = em.merge(usuarios);
            if (rolOld != null && !rolOld.equals(rolNew)) {
                rolOld.getUsuariosList().remove(usuarios);
                rolOld = em.merge(rolOld);
            }
            if (rolNew != null && !rolNew.equals(rolOld)) {
                rolNew.getUsuariosList().add(usuarios);
                rolNew = em.merge(rolNew);
            }
            for (Facturas facturasListOldFacturas : facturasListOld) {
                if (!facturasListNew.contains(facturasListOldFacturas)) {
                    facturasListOldFacturas.setUsuario(null);
                    facturasListOldFacturas = em.merge(facturasListOldFacturas);
                }
            }
            for (Facturas facturasListNewFacturas : facturasListNew) {
                if (!facturasListOld.contains(facturasListNewFacturas)) {
                    Usuarios oldUsuarioOfFacturasListNewFacturas = facturasListNewFacturas.getUsuario();
                    facturasListNewFacturas.setUsuario(usuarios);
                    facturasListNewFacturas = em.merge(facturasListNewFacturas);
                    if (oldUsuarioOfFacturasListNewFacturas != null && !oldUsuarioOfFacturasListNewFacturas.equals(usuarios)) {
                        oldUsuarioOfFacturasListNewFacturas.getFacturasList().remove(facturasListNewFacturas);
                        oldUsuarioOfFacturasListNewFacturas = em.merge(oldUsuarioOfFacturasListNewFacturas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = usuarios.getEmailusuario();
                if (findUsuarios(id) == null) {
                    throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuarios usuarios;
            try {
                usuarios = em.getReference(Usuarios.class, id);
                usuarios.getEmailusuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.", enfe);
            }
            Roles rol = usuarios.getRol();
            if (rol != null) {
                rol.getUsuariosList().remove(usuarios);
                rol = em.merge(rol);
            }
            List<Facturas> facturasList = usuarios.getFacturasList();
            for (Facturas facturasListFacturas : facturasList) {
                facturasListFacturas.setUsuario(null);
                facturasListFacturas = em.merge(facturasListFacturas);
            }
            em.remove(usuarios);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuarios> findUsuariosEntities() {
        return findUsuariosEntities(true, -1, -1);
    }

    public List<Usuarios> findUsuariosEntities(int maxResults, int firstResult) {
        return findUsuariosEntities(false, maxResults, firstResult);
    }

    private List<Usuarios> findUsuariosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuarios.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Usuarios findUsuarios(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuarios.class, id);
        } finally {
            em.close();
        }
    }
    
    public Usuarios findUsuarios(String id, String clave) {
        EntityManager em = getEntityManager();
        Usuarios usuarios = null;
        try {
            usuarios = (Usuarios) em.createNamedQuery("Usuarios.findByEmailClave")
                    .setParameter("emailusuario", id)
                    .setParameter("password", clave)
                    .getSingleResult();
            return usuarios;
        } catch (NoResultException ex) {
            return null;
        } finally {
            em.close();
        }
    }

    public int getUsuariosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuarios> rt = cq.from(Usuarios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
