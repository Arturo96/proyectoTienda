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
import com.tienda.model.Proveedores;
import com.tienda.model.Tipoproductos;
import com.tienda.model.Detallefacturas;
import com.tienda.model.Productos;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author GABRIEL
 */
public class ProductosJpaController implements Serializable {

    public ProductosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Productos productos) throws PreexistingEntityException, Exception {
        if (productos.getDetallefacturasList() == null) {
            productos.setDetallefacturasList(new ArrayList<Detallefacturas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Proveedores proveedor = productos.getProveedor();
            if (proveedor != null) {
                proveedor = em.getReference(proveedor.getClass(), proveedor.getIdprov());
                productos.setProveedor(proveedor);
            }
            Tipoproductos tipopdto = productos.getTipopdto();
            if (tipopdto != null) {
                tipopdto = em.getReference(tipopdto.getClass(), tipopdto.getIdtipopdto());
                productos.setTipopdto(tipopdto);
            }
            List<Detallefacturas> attachedDetallefacturasList = new ArrayList<Detallefacturas>();
            for (Detallefacturas detallefacturasListDetallefacturasToAttach : productos.getDetallefacturasList()) {
                detallefacturasListDetallefacturasToAttach = em.getReference(detallefacturasListDetallefacturasToAttach.getClass(), detallefacturasListDetallefacturasToAttach.getDetallefacturasPK());
                attachedDetallefacturasList.add(detallefacturasListDetallefacturasToAttach);
            }
            productos.setDetallefacturasList(attachedDetallefacturasList);
            em.persist(productos);
            if (proveedor != null) {
                proveedor.getProductosList().add(productos);
                proveedor = em.merge(proveedor);
            }
            if (tipopdto != null) {
                tipopdto.getProductosList().add(productos);
                tipopdto = em.merge(tipopdto);
            }
            for (Detallefacturas detallefacturasListDetallefacturas : productos.getDetallefacturasList()) {
                Productos oldProductosOfDetallefacturasListDetallefacturas = detallefacturasListDetallefacturas.getProductos();
                detallefacturasListDetallefacturas.setProductos(productos);
                detallefacturasListDetallefacturas = em.merge(detallefacturasListDetallefacturas);
                if (oldProductosOfDetallefacturasListDetallefacturas != null) {
                    oldProductosOfDetallefacturasListDetallefacturas.getDetallefacturasList().remove(detallefacturasListDetallefacturas);
                    oldProductosOfDetallefacturasListDetallefacturas = em.merge(oldProductosOfDetallefacturasListDetallefacturas);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProductos(productos.getIdpdto()) != null) {
                throw new PreexistingEntityException("Productos " + productos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Productos productos) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Productos persistentProductos = em.find(Productos.class, productos.getIdpdto());
            Proveedores proveedorOld = persistentProductos.getProveedor();
            Proveedores proveedorNew = productos.getProveedor();
            Tipoproductos tipopdtoOld = persistentProductos.getTipopdto();
            Tipoproductos tipopdtoNew = productos.getTipopdto();
            List<Detallefacturas> detallefacturasListOld = persistentProductos.getDetallefacturasList();
            List<Detallefacturas> detallefacturasListNew = productos.getDetallefacturasList();
            List<String> illegalOrphanMessages = null;
            for (Detallefacturas detallefacturasListOldDetallefacturas : detallefacturasListOld) {
                if (!detallefacturasListNew.contains(detallefacturasListOldDetallefacturas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Detallefacturas " + detallefacturasListOldDetallefacturas + " since its productos field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (proveedorNew != null) {
                proveedorNew = em.getReference(proveedorNew.getClass(), proveedorNew.getIdprov());
                productos.setProveedor(proveedorNew);
            }
            if (tipopdtoNew != null) {
                tipopdtoNew = em.getReference(tipopdtoNew.getClass(), tipopdtoNew.getIdtipopdto());
                productos.setTipopdto(tipopdtoNew);
            }
            List<Detallefacturas> attachedDetallefacturasListNew = new ArrayList<Detallefacturas>();
            for (Detallefacturas detallefacturasListNewDetallefacturasToAttach : detallefacturasListNew) {
                detallefacturasListNewDetallefacturasToAttach = em.getReference(detallefacturasListNewDetallefacturasToAttach.getClass(), detallefacturasListNewDetallefacturasToAttach.getDetallefacturasPK());
                attachedDetallefacturasListNew.add(detallefacturasListNewDetallefacturasToAttach);
            }
            detallefacturasListNew = attachedDetallefacturasListNew;
            productos.setDetallefacturasList(detallefacturasListNew);
            productos = em.merge(productos);
            if (proveedorOld != null && !proveedorOld.equals(proveedorNew)) {
                proveedorOld.getProductosList().remove(productos);
                proveedorOld = em.merge(proveedorOld);
            }
            if (proveedorNew != null && !proveedorNew.equals(proveedorOld)) {
                proveedorNew.getProductosList().add(productos);
                proveedorNew = em.merge(proveedorNew);
            }
            if (tipopdtoOld != null && !tipopdtoOld.equals(tipopdtoNew)) {
                tipopdtoOld.getProductosList().remove(productos);
                tipopdtoOld = em.merge(tipopdtoOld);
            }
            if (tipopdtoNew != null && !tipopdtoNew.equals(tipopdtoOld)) {
                tipopdtoNew.getProductosList().add(productos);
                tipopdtoNew = em.merge(tipopdtoNew);
            }
            for (Detallefacturas detallefacturasListNewDetallefacturas : detallefacturasListNew) {
                if (!detallefacturasListOld.contains(detallefacturasListNewDetallefacturas)) {
                    Productos oldProductosOfDetallefacturasListNewDetallefacturas = detallefacturasListNewDetallefacturas.getProductos();
                    detallefacturasListNewDetallefacturas.setProductos(productos);
                    detallefacturasListNewDetallefacturas = em.merge(detallefacturasListNewDetallefacturas);
                    if (oldProductosOfDetallefacturasListNewDetallefacturas != null && !oldProductosOfDetallefacturasListNewDetallefacturas.equals(productos)) {
                        oldProductosOfDetallefacturasListNewDetallefacturas.getDetallefacturasList().remove(detallefacturasListNewDetallefacturas);
                        oldProductosOfDetallefacturasListNewDetallefacturas = em.merge(oldProductosOfDetallefacturasListNewDetallefacturas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = productos.getIdpdto();
                if (findProductos(id) == null) {
                    throw new NonexistentEntityException("The productos with id " + id + " no longer exists.");
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
            Productos productos;
            try {
                productos = em.getReference(Productos.class, id);
                productos.getIdpdto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The productos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Detallefacturas> detallefacturasListOrphanCheck = productos.getDetallefacturasList();
            for (Detallefacturas detallefacturasListOrphanCheckDetallefacturas : detallefacturasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Productos (" + productos + ") cannot be destroyed since the Detallefacturas " + detallefacturasListOrphanCheckDetallefacturas + " in its detallefacturasList field has a non-nullable productos field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Proveedores proveedor = productos.getProveedor();
            if (proveedor != null) {
                proveedor.getProductosList().remove(productos);
                proveedor = em.merge(proveedor);
            }
            Tipoproductos tipopdto = productos.getTipopdto();
            if (tipopdto != null) {
                tipopdto.getProductosList().remove(productos);
                tipopdto = em.merge(tipopdto);
            }
            em.remove(productos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Productos> findProductosEntities() {
        return findProductosEntities(true, -1, -1);
    }

    public List<Productos> findProductosEntities(int maxResults, int firstResult) {
        return findProductosEntities(false, maxResults, firstResult);
    }

    private List<Productos> findProductosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Productos.class));
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

    public Productos findProductos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Productos.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Productos> rt = cq.from(Productos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
