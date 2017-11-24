/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tienda.jpa;

import com.tienda.jpa.exceptions.IllegalOrphanException;
import com.tienda.jpa.exceptions.NonexistentEntityException;
import com.tienda.jpa.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.tienda.model.Clientes;
import com.tienda.model.Usuarios;
import com.tienda.model.Detallefacturas;
import com.tienda.model.Facturas;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

/**
 *
 * @author GABRIEL
 */
public class FacturasJpaController implements Serializable {

    public FacturasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Facturas facturas) throws PreexistingEntityException, Exception {
        if (facturas.getDetallefacturasList() == null) {
            facturas.setDetallefacturasList(new ArrayList<Detallefacturas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Clientes cliente = facturas.getCliente();
            if (cliente != null) {
                cliente = em.getReference(cliente.getClass(), cliente.getNrodocumento());
                facturas.setCliente(cliente);
            }
            Usuarios usuario = facturas.getUsuario();
            if (usuario != null) {
                usuario = em.getReference(usuario.getClass(), usuario.getEmailusuario());
                facturas.setUsuario(usuario);
            }
            List<Detallefacturas> attachedDetallefacturasList = new ArrayList<Detallefacturas>();
            for (Detallefacturas detallefacturasListDetallefacturasToAttach : facturas.getDetallefacturasList()) {
                detallefacturasListDetallefacturasToAttach = em.getReference(detallefacturasListDetallefacturasToAttach.getClass(), detallefacturasListDetallefacturasToAttach.getDetallefacturasPK());
                attachedDetallefacturasList.add(detallefacturasListDetallefacturasToAttach);
            }
            facturas.setDetallefacturasList(attachedDetallefacturasList);
            em.persist(facturas);
            if (cliente != null) {
                cliente.getFacturasList().add(facturas);
                cliente = em.merge(cliente);
            }
            if (usuario != null) {
                usuario.getFacturasList().add(facturas);
                usuario = em.merge(usuario);
            }
            for (Detallefacturas detallefacturasListDetallefacturas : facturas.getDetallefacturasList()) {
                Facturas oldFacturasOfDetallefacturasListDetallefacturas = detallefacturasListDetallefacturas.getFacturas();
                detallefacturasListDetallefacturas.setFacturas(facturas);
                detallefacturasListDetallefacturas = em.merge(detallefacturasListDetallefacturas);
                if (oldFacturasOfDetallefacturasListDetallefacturas != null) {
                    oldFacturasOfDetallefacturasListDetallefacturas.getDetallefacturasList().remove(detallefacturasListDetallefacturas);
                    oldFacturasOfDetallefacturasListDetallefacturas = em.merge(oldFacturasOfDetallefacturasListDetallefacturas);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFacturas(facturas.getIdfactura()) != null) {
                throw new PreexistingEntityException("Facturas " + facturas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Facturas facturas) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Facturas persistentFacturas = em.find(Facturas.class, facturas.getIdfactura());
            Clientes clienteOld = persistentFacturas.getCliente();
            Clientes clienteNew = facturas.getCliente();
            Usuarios usuarioOld = persistentFacturas.getUsuario();
            Usuarios usuarioNew = facturas.getUsuario();
            List<Detallefacturas> detallefacturasListOld = persistentFacturas.getDetallefacturasList();
            List<Detallefacturas> detallefacturasListNew = facturas.getDetallefacturasList();
            List<String> illegalOrphanMessages = null;
            for (Detallefacturas detallefacturasListOldDetallefacturas : detallefacturasListOld) {
                if (!detallefacturasListNew.contains(detallefacturasListOldDetallefacturas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Detallefacturas " + detallefacturasListOldDetallefacturas + " since its facturas field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (clienteNew != null) {
                clienteNew = em.getReference(clienteNew.getClass(), clienteNew.getNrodocumento());
                facturas.setCliente(clienteNew);
            }
            if (usuarioNew != null) {
                usuarioNew = em.getReference(usuarioNew.getClass(), usuarioNew.getEmailusuario());
                facturas.setUsuario(usuarioNew);
            }
            List<Detallefacturas> attachedDetallefacturasListNew = new ArrayList<Detallefacturas>();
            for (Detallefacturas detallefacturasListNewDetallefacturasToAttach : detallefacturasListNew) {
                detallefacturasListNewDetallefacturasToAttach = em.getReference(detallefacturasListNewDetallefacturasToAttach.getClass(), detallefacturasListNewDetallefacturasToAttach.getDetallefacturasPK());
                attachedDetallefacturasListNew.add(detallefacturasListNewDetallefacturasToAttach);
            }
            detallefacturasListNew = attachedDetallefacturasListNew;
            facturas.setDetallefacturasList(detallefacturasListNew);
            facturas = em.merge(facturas);
            if (clienteOld != null && !clienteOld.equals(clienteNew)) {
                clienteOld.getFacturasList().remove(facturas);
                clienteOld = em.merge(clienteOld);
            }
            if (clienteNew != null && !clienteNew.equals(clienteOld)) {
                clienteNew.getFacturasList().add(facturas);
                clienteNew = em.merge(clienteNew);
            }
            if (usuarioOld != null && !usuarioOld.equals(usuarioNew)) {
                usuarioOld.getFacturasList().remove(facturas);
                usuarioOld = em.merge(usuarioOld);
            }
            if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
                usuarioNew.getFacturasList().add(facturas);
                usuarioNew = em.merge(usuarioNew);
            }
            for (Detallefacturas detallefacturasListNewDetallefacturas : detallefacturasListNew) {
                if (!detallefacturasListOld.contains(detallefacturasListNewDetallefacturas)) {
                    Facturas oldFacturasOfDetallefacturasListNewDetallefacturas = detallefacturasListNewDetallefacturas.getFacturas();
                    detallefacturasListNewDetallefacturas.setFacturas(facturas);
                    detallefacturasListNewDetallefacturas = em.merge(detallefacturasListNewDetallefacturas);
                    if (oldFacturasOfDetallefacturasListNewDetallefacturas != null && !oldFacturasOfDetallefacturasListNewDetallefacturas.equals(facturas)) {
                        oldFacturasOfDetallefacturasListNewDetallefacturas.getDetallefacturasList().remove(detallefacturasListNewDetallefacturas);
                        oldFacturasOfDetallefacturasListNewDetallefacturas = em.merge(oldFacturasOfDetallefacturasListNewDetallefacturas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = facturas.getIdfactura();
                if (findFacturas(id) == null) {
                    throw new NonexistentEntityException("The facturas with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Facturas facturas;
            try {
                facturas = em.getReference(Facturas.class, id);
                facturas.getIdfactura();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The facturas with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Detallefacturas> detallefacturasListOrphanCheck = facturas.getDetallefacturasList();
            for (Detallefacturas detallefacturasListOrphanCheckDetallefacturas : detallefacturasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Facturas (" + facturas + ") cannot be destroyed since the Detallefacturas " + detallefacturasListOrphanCheckDetallefacturas + " in its detallefacturasList field has a non-nullable facturas field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Clientes cliente = facturas.getCliente();
            if (cliente != null) {
                cliente.getFacturasList().remove(facturas);
                cliente = em.merge(cliente);
            }
            Usuarios usuario = facturas.getUsuario();
            if (usuario != null) {
                usuario.getFacturasList().remove(facturas);
                usuario = em.merge(usuario);
            }
            em.remove(facturas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Facturas> findFacturasEntities() {
        return findFacturasEntities(true, -1, -1);
    }

    public List<Facturas> findFacturasEntities(int maxResults, int firstResult) {
        return findFacturasEntities(false, maxResults, firstResult);
    }

    private List<Facturas> findFacturasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Facturas.class));
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

    public Facturas findFacturas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Facturas.class, id);
        } finally {
            em.close();
        }
    }
    
    public List<Facturas> findFacturasbyCliente(Clientes cliente) {
        EntityManager em = getEntityManager();
        List<Facturas> listado = null;
        try {
            listado = (List<Facturas>)em.createNamedQuery("Facturas.findByCliente")
                    .setParameter("cliente", cliente)
                    .getResultList();
            return listado;
        } catch(NoResultException ex) {
            return null;
        } finally {
            em.close();
        }
        
        
    }

    public int getFacturasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Facturas> rt = cq.from(Facturas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
