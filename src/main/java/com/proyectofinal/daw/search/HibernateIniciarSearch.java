package com.proyectofinal.daw.search;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import com.proyectofinal.daw.entities.Autor;

import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/*Al iniciar la aplicación ira a la BD e indexará los datos de alli
interesante si se tienen datos anteriores o se hace un volcado maxivo en la BD
sin pasar por el servidor.*/

@Component
public class HibernateIniciarSearch implements ApplicationListener<ContextRefreshedEvent> {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override

    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            SearchSession searchSession = Search.session(entityManager);
            MassIndexer indexer = searchSession.massIndexer(Autor.class).threadsToLoadObjects(1);
            indexer.startAndWait();
        } catch (InterruptedException e) {
            System.out.println("Error hibernate search" + e.toString());
        }
    }
}
