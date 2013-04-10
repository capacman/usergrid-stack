package org.usergrid.dashboard.service;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.usergrid.dashboard.domain.UsergridCounter;
import org.usergrid.management.ApplicationInfo;
import org.usergrid.management.OrganizationInfo;
import org.usergrid.management.OrganizationOwnerInfo;
import org.usergrid.management.UserInfo;

public class DashboardServiceImpl implements DashboardService {

    private static final Logger logger = LoggerFactory
            .getLogger(DashboardServiceImpl.class);
    private EntityManager em;

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Override
    public void applicationCreated(ApplicationInfo applicationInfo) {
        checkAndUpdate(APPLICATIONS_COUNTER);
    }

    @Override
    public void organizationCreated(OrganizationInfo organizationInfo) {
        checkAndUpdate(ORGANIZATIONS_COUNTER);
    }

    @Override
    public void organizationOwnerCreated(
            OrganizationOwnerInfo organizationOwnerInfo) {
        checkAndUpdate(ADMINUSER_COUNTER);
        checkAndUpdate(ORGANIZATIONS_COUNTER);
    }

    @Override
    public void adminUserCreated(UserInfo userInfo) {
        checkAndUpdate(ADMINUSER_COUNTER);
    }

    @Override
    public List<UsergridCounter> getDashboardCounters() {
        Query query = em.createQuery("select c From UsergridCounter c");
        return query.getResultList();
    }

    @Autowired
    public void setEntityManagerFactory(EntityManager em) {
        this.em = em;
    }

    @Override
    public void appUserCreated(ApplicationInfo applicationInfo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void checkAndUpdate(String counterName) {
        int counter = prepareQuery(counterName).executeUpdate();
        if (counter <= 0) {
            createCounter(counterName);
            prepareQuery(counterName).executeUpdate();
        }
    }

    private Query prepareQuery(String counterName) {
        return em.createQuery("UPDATE UsergridCounter c SET c.counter=c.counter+1 WHERE c.name='" + counterName + "'");
    }

    private void createCounter(String counterName) {
        UsergridCounter counter = new UsergridCounter();
        counter.setCounter(0);
        counter.setName(counterName);
        try {
            em.persist(counter);
        } catch (DataIntegrityViolationException e) {
            logger.warn("counter {} already created", counterName);
        }
    }
}
