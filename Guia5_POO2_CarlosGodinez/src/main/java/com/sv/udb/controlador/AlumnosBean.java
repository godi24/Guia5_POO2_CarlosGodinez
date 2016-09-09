/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.controlador;

import com.sv.udb.modelo.Alumnos;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import org.primefaces.context.RequestContext;

/**
 *
 * @author REGISTRO
 */
@Named(value = "alumnosBean")
@ViewScoped
@ManagedBean
public class AlumnosBean implements Serializable{
    private Alumnos objeAlum;
    private boolean guardar;
    private List<Alumnos> alumList = null;

    public Alumnos getObjeAlum() {
        return objeAlum;
    }

    public void setObjeAlum(Alumnos objeAlum) {
        this.objeAlum = objeAlum;
    }

    public boolean isGuardar() {
        return guardar;
    }

    public List<Alumnos> getAlumList() {
        return alumList;
    }

    public void setAlumList(List<Alumnos> alumList) {
        this.alumList = alumList;
    }
    
    /**
     * Creates a new instance of AlumnosBean
     */
    
    public AlumnosBean() {
    }
    
    @PostConstruct
    public void init()
    {
        this.objeAlum = new Alumnos();
        this.guardar = true;
        this.alumList = new ArrayList<>();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PARCIALPU");
        EntityManager em = emf.createEntityManager();
        try
        {
           TypedQuery<Alumnos> query =em.createNamedQuery("Alumnos.findAll", Alumnos.class);
           this.alumList = query.getResultList();
        }
        catch(Exception ex)
        {
            
        }
    }
    
    public void guar()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturar el contexto
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PARCIALPU");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try
        {
            em.persist(this.objeAlum);
            tx.commit();
            this.guardar = true;
            this.alumList.add(this.objeAlum);
            this.objeAlum = new Alumnos();
            ctx.execute("setMessage('MESS_SUCC', 'Alerta', 'Datos guardados con éxito.');");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error.');");
            tx.rollback();
            ex.printStackTrace();
        }
        finally
        {
            em.close();
            emf.close();            
        }
    }
    
    public void modi(int codi)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PARCIALPU");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try
        {
            Alumnos objAlum = em.find(Alumnos.class, codi);
            em.merge(objAlum);
            tx.commit();
            this.alumList = new ArrayList<>();
            EntityManagerFactory emf1 = Persistence.createEntityManagerFactory("PARCIALPU");
            EntityManager em1 = emf1.createEntityManager();
            try
            {
               TypedQuery<Alumnos> query =em1.createNamedQuery("Alumnos.findAll", Alumnos.class);
               this.alumList = query.getResultList();
            }
            catch(Exception ex)
            {

            }
            this.objeAlum = new Alumnos();
        }
        catch(Exception ex)
        {
            tx.rollback();
        }
        em.close();
        emf.close();
    }
    
    public void elim(int codi)
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturar el contexto
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PARCIALPU");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try
        {
            Alumnos objAlum = em.find(Alumnos.class, codi);
            em.remove(objAlum);
            tx.commit();
            this.alumList = new ArrayList<>();
            EntityManagerFactory emf1 = Persistence.createEntityManagerFactory("PARCIALPU");
            EntityManager em1 = emf1.createEntityManager();
            try
            {
               TypedQuery<Alumnos> query =em1.createNamedQuery("Alumnos.findAll", Alumnos.class);
               this.alumList = query.getResultList();
            }
            catch(Exception ex)
            {

            }
            this.objeAlum = new Alumnos();
            ctx.execute("setMessage('MESS_SUCC', 'Alerta', 'Registro eliminado con éxito.');");
        }
        catch(Exception ex)
        {
            tx.rollback();
        }
        em.close();
        emf.close();
    }
    
    public void cons(int codi)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PARCIALPU");
        EntityManager em = emf.createEntityManager();
        try
        {
            this.objeAlum = em.find(Alumnos.class, codi);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            em.close();
            emf.close();            
        }
    }
}
