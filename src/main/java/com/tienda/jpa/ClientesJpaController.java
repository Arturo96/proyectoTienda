/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tienda.jpa;

import com.tienda.jpa.exceptions.NonexistentEntityException;
import com.tienda.jpa.exceptions.PreexistingEntityException;
import com.tienda.model.Clientes;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.tienda.model.Tipoidclientes;
import com.tienda.model.Facturas;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author GABRIEL
 */
public class ClientesJpaController implements Serializable {

    public ClientesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Clientes clientes) throws PreexistingEntityException, Exception {
        if (clientes.getFacturasList() == null) {
            clientes.setFacturasList(new ArrayList<Facturas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tipoidclientes tipodocumento = clientes.getTipodocumento();
            if (tipodocumento != null) {
                tipodocumento = em.getReference(tipodocumento.getClass(), tipodocumento.getIdtipodocumento());
                clientes.setTipodocumento(tipodocumento);
            }
            List<Facturas> attachedFacturasList = new ArrayList<Facturas>();
            for (Facturas facturasListFacturasToAttach : clientes.getFacturasList()) {
                facturasListFacturasToAttach = em.getReference(facturasListFacturasToAttach.getClass(), facturasListFacturasToAttach.getIdfactura());
                attachedFacturasList.add(facturasListFacturasToAttach);
            }
            clientes.setFacturasList(attachedFacturasList);
            em.persist(clientes);
            if (tipodocumento != null) {
                tipodocumento.getClientesList().add(clientes);
                tipodocumento = em.merge(tipodocumento);
            }
            for (Facturas facturasListFacturas : clientes.getFacturasList()) {
                Clientes oldClienteOfFacturasListFacturas = facturasListFacturas.getCliente();
                facturasListFacturas.setCliente(clientes);
                facturasListFacturas = em.merge(facturasListFacturas);
                if (oldClienteOfFacturasListFacturas != null) {
                    oldClienteOfFacturasListFacturas.getFacturasList().remove(facturasListFacturas);
                    oldClienteOfFacturasListFacturas = em.merge(oldClienteOfFacturasListFacturas);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findClientes(clientes.getNrodocumento()) != null) {
                throw new PreexistingEntityException("Clientes " + clientes + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Clientes clientes) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Clientes persistentClientes = em.find(Clientes.class, clientes.getNrodocumento());
            Tipoidclientes tipodocumentoOld = persistentClientes.getTipodocumento();
            Tipoidclientes tipodocumentoNew = clientes.getTipodocumento();
            List<Facturas> facturasListOld = persistentClientes.getFacturasList();
            List<Facturas> facturasListNew = clientes.getFacturasList();
            if (tipodocumentoNew != null) {
                tipodocumentoNew = em.getReference(tipodocumentoNew.getClass(), tipodocumentoNew.getIdtipodocumento());
                clientes.setTipodocumento(tipodocumentoNew);
            }
            List<Facturas> attachedFacturasListNew = new ArrayList<Facturas>();
            for (Facturas facturasListNewFacturasToAttach : facturasListNew) {
                facturasListNewFacturasToAttach = em.getReference(facturasListNewFacturasToAttach.getClass(), facturasListNewFacturasToAttach.getIdfactura());
                attachedFacturasListNew.add(facturasListNewFacturasToAttach);
            }
            facturasListNew = attachedFacturasListNew;
            clientes.setFacturasList(facturasListNew);
            clientes = em.merge(clientes);
            if (tipodocumentoOld != null && !tipodocumentoOld.equals(tipodocumentoNew)) {
                tipodocumentoOld.getClientesList().remove(clientes);
                tipodocumentoOld = em.merge(tipodocumentoOld);
            }
            if (tipodocumentoNew != null && !tipodocumentoNew.equals(tipodocumentoOld)) {
                tipodocumentoNew.getClientesList().add(clientes);
                tipodocumentoNew = em.merge(tipodocumentoNew);
            }
            for (Facturas facturasListOldFacturas : facturasListOld) {
                if (!facturasListNew.contains(facturasListOldFacturas)) {
                    facturasListOldFacturas.setCliente(null);
                    facturasListOldFacturas = em.merge(facturasListOldFacturas);
                }
            }
            for (Facturas facturasListNewFacturas : facturasListNew) {
                if (!facturasListOld.contains(facturasListNewFacturas)) {
                    Clientes oldClienteOfFacturasListNewFacturas = facturasListNewFacturas.getCliente();
                    facturasListNewFacturas.setCliente(clientes);
                    facturasListNewFacturas = em.merge(facturasListNewFacturas);
                    if (oldClienteOfFacturasListNewFacturas != null && !oldClienteOfFacturasListNewFacturas.equals(clientes)) {
                        oldClienteOfFacturasListNewFacturas.getFacturasList().remove(facturasListNewFacturas);
                        oldClienteOfFacturasListNewFacturas = em.merge(oldClienteOfFacturasListNewFacturas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = clientes.getNrodocumento();
                if (findClientes(id) == null) {
                    throw new NonexistentEntityException("The clientes with id " + id + " no longer exists.");
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
            Clientes clientes;
            try {
                clientes = em.getReference(Clientes.class, id);
                clientes.getNrodocumento();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The clientes with id " + id + " no longer exists.", enfe);
            }
            Tipoidclientes tipodocumento = clientes.getTipodocumento();
            if (tipodocumento != null) {
                tipodocumento.getClientesList().remove(clientes);
                tipodocumento = em.merge(tipodocumento);
            }
            List<Facturas> facturasList = clientes.getFacturasList();
            for (Facturas facturasListFacturas : facturasList) {
                facturasListFacturas.setCliente(null);
                facturasListFacturas = em.merge(facturasListFacturas);
            }
            em.remove(clientes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Clientes> findClientesEntities() {
        return findClientesEntities(true, -1, -1);
    }

    public List<Clientes> findClientesEntities(int maxResults, int firstResult) {
        return findClientesEntities(false, maxResults, firstResult);
    }

    private List<Clientes> findClientesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Clientes.class));
            cq.orderBy(em.getCriteriaBuilder().asc(cq.from(Clientes.class).get("nrodocumento")));
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

    public Clientes findClientes(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Clientes.class, id);
        } finally {
            em.close();
        }
    }

    public int getClientesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Clientes> rt = cq.from(Clientes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
