/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tienda.jpa;

import com.tienda.jpa.exceptions.NonexistentEntityException;
import com.tienda.jpa.exceptions.PreexistingEntityException;
import com.tienda.model.Detallefacturas;
import com.tienda.model.DetallefacturasPK;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.tienda.model.Facturas;
import com.tienda.model.Productos;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

/**
 *
 * @author GABRIEL
 */
public class DetallefacturasJpaController implements Serializable {

    public DetallefacturasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Detallefacturas detallefacturas) throws PreexistingEntityException, Exception {
        if (detallefacturas.getDetallefacturasPK() == null) {
            detallefacturas.setDetallefacturasPK(new DetallefacturasPK());
        }
        detallefacturas.getDetallefacturasPK().setFactura(detallefacturas.getFacturas().getIdfactura());
        detallefacturas.getDetallefacturasPK().setProducto(detallefacturas.getProductos().getIdpdto());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Facturas facturas = detallefacturas.getFacturas();
            if (facturas != null) {
                facturas = em.getReference(facturas.getClass(), facturas.getIdfactura());
                detallefacturas.setFacturas(facturas);
            }
            Productos productos = detallefacturas.getProductos();
            if (productos != null) {
                productos = em.getReference(productos.getClass(), productos.getIdpdto());
                detallefacturas.setProductos(productos);
            }
            em.persist(detallefacturas);
            if (facturas != null) {
                facturas.getDetallefacturasList().add(detallefacturas);
                facturas = em.merge(facturas);
            }
            if (productos != null) {
                productos.getDetallefacturasList().add(detallefacturas);
                productos = em.merge(productos);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDetallefacturas(detallefacturas.getDetallefacturasPK()) != null) {
                throw new PreexistingEntityException("Detallefacturas " + detallefacturas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Detallefacturas detallefacturas) throws NonexistentEntityException, Exception {
        detallefacturas.getDetallefacturasPK().setFactura(detallefacturas.getFacturas().getIdfactura());
        detallefacturas.getDetallefacturasPK().setProducto(detallefacturas.getProductos().getIdpdto());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Detallefacturas persistentDetallefacturas = em.find(Detallefacturas.class, detallefacturas.getDetallefacturasPK());
            Facturas facturasOld = persistentDetallefacturas.getFacturas();
            Facturas facturasNew = detallefacturas.getFacturas();
            Productos productosOld = persistentDetallefacturas.getProductos();
            Productos productosNew = detallefacturas.getProductos();
            if (facturasNew != null) {
                facturasNew = em.getReference(facturasNew.getClass(), facturasNew.getIdfactura());
                detallefacturas.setFacturas(facturasNew);
            }
            if (productosNew != null) {
                productosNew = em.getReference(productosNew.getClass(), productosNew.getIdpdto());
                detallefacturas.setProductos(productosNew);
            }
            detallefacturas = em.merge(detallefacturas);
            if (facturasOld != null && !facturasOld.equals(facturasNew)) {
                facturasOld.getDetallefacturasList().remove(detallefacturas);
                facturasOld = em.merge(facturasOld);
            }
            if (facturasNew != null && !facturasNew.equals(facturasOld)) {
                facturasNew.getDetallefacturasList().add(detallefacturas);
                facturasNew = em.merge(facturasNew);
            }
            if (productosOld != null && !productosOld.equals(productosNew)) {
                productosOld.getDetallefacturasList().remove(detallefacturas);
                productosOld = em.merge(productosOld);
            }
            if (productosNew != null && !productosNew.equals(productosOld)) {
                productosNew.getDetallefacturasList().add(detallefacturas);
                productosNew = em.merge(productosNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                DetallefacturasPK id = detallefacturas.getDetallefacturasPK();
                if (findDetallefacturas(id) == null) {
                    throw new NonexistentEntityException("The detallefacturas with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(DetallefacturasPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Detallefacturas detallefacturas;
            try {
                detallefacturas = em.getReference(Detallefacturas.class, id);
                detallefacturas.getDetallefacturasPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detallefacturas with id " + id + " no longer exists.", enfe);
            }
            Facturas facturas = detallefacturas.getFacturas();
            if (facturas != null) {
                facturas.getDetallefacturasList().remove(detallefacturas);
                facturas = em.merge(facturas);
            }
            Productos productos = detallefacturas.getProductos();
            if (productos != null) {
                productos.getDetallefacturasList().remove(detallefacturas);
                productos = em.merge(productos);
            }
            em.remove(detallefacturas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Detallefacturas> findDetallefacturasEntities() {
        return findDetallefacturasEntities(true, -1, -1);
    }

    public List<Detallefacturas> findDetallefacturasEntities(int maxResults, int firstResult) {
        return findDetallefacturasEntities(false, maxResults, firstResult);
    }

    private List<Detallefacturas> findDetallefacturasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Detallefacturas.class));
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

    public Detallefacturas findDetallefacturas(DetallefacturasPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Detallefacturas.class, id);
        } finally {
            em.close();
        }
    }
    
    public List<Detallefacturas> findDetallefacturasbyFactura(Facturas factura) {
        EntityManager em = getEntityManager();
        List<Detallefacturas> listado = null; 
       try {
           listado = (List<Detallefacturas>)
                   em.createNamedQuery("Detallefacturas.findByIdFactura")
                     .setParameter("factura", factura)
                     .getResultList();
           return listado;
       } catch (NoResultException ex) {
           return null;
       } finally {
           em.close();
       }
    }

    public int getDetallefacturasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Detallefacturas> rt = cq.from(Detallefacturas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
