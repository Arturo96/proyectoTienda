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
import com.tienda.model.Productos;
import com.tienda.model.Tipoproductos;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author GABRIEL
 */
public class TipoproductosJpaController implements Serializable {

    public TipoproductosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tipoproductos tipoproductos) throws PreexistingEntityException, Exception {
        if (tipoproductos.getProductosList() == null) {
            tipoproductos.setProductosList(new ArrayList<Productos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Productos> attachedProductosList = new ArrayList<Productos>();
            for (Productos productosListProductosToAttach : tipoproductos.getProductosList()) {
                productosListProductosToAttach = em.getReference(productosListProductosToAttach.getClass(), productosListProductosToAttach.getIdpdto());
                attachedProductosList.add(productosListProductosToAttach);
            }
            tipoproductos.setProductosList(attachedProductosList);
            em.persist(tipoproductos);
            for (Productos productosListProductos : tipoproductos.getProductosList()) {
                Tipoproductos oldTipopdtoOfProductosListProductos = productosListProductos.getTipopdto();
                productosListProductos.setTipopdto(tipoproductos);
                productosListProductos = em.merge(productosListProductos);
                if (oldTipopdtoOfProductosListProductos != null) {
                    oldTipopdtoOfProductosListProductos.getProductosList().remove(productosListProductos);
                    oldTipopdtoOfProductosListProductos = em.merge(oldTipopdtoOfProductosListProductos);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTipoproductos(tipoproductos.getIdtipopdto()) != null) {
                throw new PreexistingEntityException("Tipoproductos " + tipoproductos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tipoproductos tipoproductos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tipoproductos persistentTipoproductos = em.find(Tipoproductos.class, tipoproductos.getIdtipopdto());
            List<Productos> productosListOld = persistentTipoproductos.getProductosList();
            List<Productos> productosListNew = tipoproductos.getProductosList();
            List<Productos> attachedProductosListNew = new ArrayList<Productos>();
            for (Productos productosListNewProductosToAttach : productosListNew) {
                productosListNewProductosToAttach = em.getReference(productosListNewProductosToAttach.getClass(), productosListNewProductosToAttach.getIdpdto());
                attachedProductosListNew.add(productosListNewProductosToAttach);
            }
            productosListNew = attachedProductosListNew;
            tipoproductos.setProductosList(productosListNew);
            tipoproductos = em.merge(tipoproductos);
            for (Productos productosListOldProductos : productosListOld) {
                if (!productosListNew.contains(productosListOldProductos)) {
                    productosListOldProductos.setTipopdto(null);
                    productosListOldProductos = em.merge(productosListOldProductos);
                }
            }
            for (Productos productosListNewProductos : productosListNew) {
                if (!productosListOld.contains(productosListNewProductos)) {
                    Tipoproductos oldTipopdtoOfProductosListNewProductos = productosListNewProductos.getTipopdto();
                    productosListNewProductos.setTipopdto(tipoproductos);
                    productosListNewProductos = em.merge(productosListNewProductos);
                    if (oldTipopdtoOfProductosListNewProductos != null && !oldTipopdtoOfProductosListNewProductos.equals(tipoproductos)) {
                        oldTipopdtoOfProductosListNewProductos.getProductosList().remove(productosListNewProductos);
                        oldTipopdtoOfProductosListNewProductos = em.merge(oldTipopdtoOfProductosListNewProductos);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipoproductos.getIdtipopdto();
                if (findTipoproductos(id) == null) {
                    throw new NonexistentEntityException("The tipoproductos with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tipoproductos tipoproductos;
            try {
                tipoproductos = em.getReference(Tipoproductos.class, id);
                tipoproductos.getIdtipopdto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoproductos with id " + id + " no longer exists.", enfe);
            }
            List<Productos> productosList = tipoproductos.getProductosList();
            for (Productos productosListProductos : productosList) {
                productosListProductos.setTipopdto(null);
                productosListProductos = em.merge(productosListProductos);
            }
            em.remove(tipoproductos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tipoproductos> findTipoproductosEntities() {
        return findTipoproductosEntities(true, -1, -1);
    }

    public List<Tipoproductos> findTipoproductosEntities(int maxResults, int firstResult) {
        return findTipoproductosEntities(false, maxResults, firstResult);
    }

    private List<Tipoproductos> findTipoproductosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tipoproductos.class));
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

    public Tipoproductos findTipoproductos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tipoproductos.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoproductosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tipoproductos> rt = cq.from(Tipoproductos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
