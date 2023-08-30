package com.flab.rallymate;

import jakarta.persistence.EntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest(properties = {"spring.profiles.active=dev"})
public abstract class StoreTest {

    protected void flushAndClear(EntityManager entityManager) {
        entityManager.flush();
        entityManager.clear();
    }
}