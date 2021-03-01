package com.wsiz.gameshub.index;

import com.wsiz.gameshub.model.entity.Game;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Profile("rebuildIndex")
@Component
public class IndexBuilder implements ApplicationRunner {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void run(ApplicationArguments args){
        SearchSession searchSession = Search.session( entityManager );
        try {
            searchSession.massIndexer(Game.class)
                    .startAndWait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
