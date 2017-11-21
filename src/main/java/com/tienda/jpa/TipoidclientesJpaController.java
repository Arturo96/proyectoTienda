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
import com.tienda.model.Clientes;
import com.tienda.model.Tipoidclientes;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author GABRIEL
 */
public class TipoidclientesJpaController implements Serializable {

    public TipoidclientesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tipoidclientes tipoidclientes) throws PreexistingEntityException, Exception {
        if (tipoidclientes.getClientesList() == null) {
            tipoidclientes.setClientesList(new ArrayList<Clientes>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Clientes> attachedClientesList = new ArrayList<Clientes>();
            for (Clientes clientesListClientesToAttach : tipoidclientes.getClientesList()) {
                clientesListClientesToAttach = em.getReference(clientesListClientesToAttach.getClass(), clientesListClientesToAttach.getNrodocumento());
                attachedClientesList.add(clientesListClientesToAttach);
            }
            tipoidclientes.setClientesList(attachedClientesList);
            em.persist(tipoidclientes);
            for (Clientes clientesListClientes : tipoidclientes.getClientesList()) {
                Tipoidclientes oldTipodocumentoOfClientesListClientes = clientesListClientes.getTipodocumento();
                clientesListClientes.setTipodocumento(tipoidclientes);
                clientesListClientes = em.merge(clientesListClientes);
                if (oldTipodocumentoOfClientesListClientes != null) {
                    oldTipodocumentoOfClientesListClientes.getClientesList().remove(clientesListClientes);
                    oldTipodocumentoOfClientesListClientes = em.merge(oldTipodocumentoOfClientesListClientes);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTipoidclientes(tipoidclientes.getIdtipodocumento()) != null) {
                throw new PreexistingEntityException("Tipoidclientes " + tipoidclientes + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tipoidclientes tipoidclientes) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tipoidclientes persistentTipoidclientes = em.find(Tipoidclientes.class, tipoidclientes.getIdtipodocumento());
            List<Clientes> clientesListOld = persistentTipoidclientes.getClientesList();
            List<Clientes> clientesListNew = tipoidclientes.getClientesList();
            List<Clientes> attachedClientesListNew = new ArrayList<Clientes>();
            for (Clientes clientesListNewClientesToAttach : clientesListNew) {
                clientesListNewClientesToAttach = em.getReference(clientesListNewClientesToAttach.getClass(), clientesListNewClientesToAttach.getNrodocumento());
                attachedClientesListNew.add(clientesListNewClientesToAttach);
            }
            clientesListNew = attachedClientesListNew;
            tipoidclientes.setClientesList(clientesListNew);
            tipoidclientes = em.merge(tipoidclientes);
            for (Clientes clientesListOldClientes : clientesListOld) {
                if (!clientesListNew.contains(clientesListOldClientes)) {
                    clientesListOldClientes.setTipodocumento(null);
                    clientesListOldClientes = em.merge(clientesListOldClientes);
                }
            }
            for (Clientes clientesListNewClientes : clientesListNew) {
                if (!clientesListOld.contains(clientesListNewClientes)) {
                    Tipoidclientes oldTipodocumentoOfClientesListNewClientes = clientesListNewClientes.getTipodocumento();
                    clientesListNewClientes.setTipodocumento(tipoidclientes);
                    clientesListNewClientes = em.merge(clientesListNewClientes);
                    if (oldTipodocumentoOfClientesListNewClientes != null && !oldTipodocumentoOfClientesListNewClientes.equals(tipoidclientes)) {
                        oldTipodocumentoOfClientesListNewClientes.getClientesList().remove(clientesListNewClientes);
                        oldTipodocumentoOfClientesListNewClientes = em.merge(oldTipodocumentoOfClientesListNewClientes);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipoidclientes.getIdtipodocumento();
                if (findTipoidclientes(id) == null) {
                    throw new NonexistentEntityException("The tipoidclientes with id " + id + " no longer exists.");
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
            Tipoidclientes tipoidclientes;
            try {
                tipoidclientes = em.getReference(Tipoidclientes.class, id);
                tipoidclientes.getIdtipodocumento();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoidclientes with id " + id + " no longer exists.", enfe);
            }
            List<Clientes> clientesList = tipoidclientes.getClientesList();
            for (Clientes clientesListClientes : clientesList) {
                clientesListClientes.setTipodocumento(null);
                clientesListClientes = em.merge(clientesListClientes);
            }
            em.remove(tipoidclientes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tipoidclientes> findTipoidclientesEntities() {
        return findTipoidclientesEntities(true, -1, -1);
    }

    public List<Tipoidclientes> findTipoidclientesEntities(int maxResults, int firstResult) {
        return findTipoidclientesEntities(false, maxResults, firstResult);
    }

    private List<Tipoidclientes> findTipoidclientesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tipoidclientes.class));
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

    public Tipoidclientes findTipoidclientes(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tipoidclientes.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoidclientesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tipoidclientes> rt = cq.from(Tipoidclientes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
