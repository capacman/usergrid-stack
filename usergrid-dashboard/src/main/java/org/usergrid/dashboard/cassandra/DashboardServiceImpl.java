package org.usergrid.dashboard.cassandra;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.usergrid.dashboard.DashboardCounters;
import org.usergrid.dashboard.DashboardService;
import org.usergrid.management.ApplicationInfo;
import org.usergrid.management.OrganizationInfo;
import org.usergrid.management.OrganizationOwnerInfo;
import org.usergrid.management.UserInfo;
import org.usergrid.persistence.EntityManager;
import org.usergrid.persistence.EntityManagerFactory;

public class DashboardServiceImpl implements DashboardService {
	private static final Logger logger = LoggerFactory
			.getLogger(DashboardServiceImpl.class);
	private static final String ADMIN_USER_COUNT = "adminUserCount";
	private static final String ORGANIZATION_COUNT = "organizationCount";
	private static final String APPLICATION_COUNT = "applicationCount";
	private static final UUID DASHBOARD_APPLICATION = new UUID(0, 2);
	private EntityManagerFactory emf;

	@Override
	public void applicationCreated(ApplicationInfo applicationInfo) {
		EntityManager em = emf.getEntityManager(DASHBOARD_APPLICATION);
		em.incrementAggregateCounters(null, null, null, APPLICATION_COUNT, 1);
	}

	@Override
	public void organizationCreated(OrganizationInfo organizationInfo) {
		EntityManager em = emf.getEntityManager(DASHBOARD_APPLICATION);
		em.incrementAggregateCounters(null, null, null, ORGANIZATION_COUNT, 1);

	}

	@Override
	public void organizationOwnerCreated(
			OrganizationOwnerInfo organizationOwnerInfo) {
		EntityManager em = emf.getEntityManager(DASHBOARD_APPLICATION);
		em.incrementAggregateCounters(null, null, null, ORGANIZATION_COUNT, 1);
		em.incrementAggregateCounters(null, null, null, ADMIN_USER_COUNT, 1);

	}

	@Override
	public void adminUserCreated(UserInfo userInfo) {
		EntityManager em = emf.getEntityManager(DASHBOARD_APPLICATION);
		em.incrementAggregateCounters(null, null, null, ADMIN_USER_COUNT, 1);
	}

	@Override
	public DashboardCounters getDashboardCounters() throws Exception {
		EntityManager em = emf.getEntityManager(DASHBOARD_APPLICATION);
		Map<String, Long> ac = em.getApplicationCounters();
		logger.info("===========");
		for (Entry<String, Long> e : ac.entrySet()) {
			logger.info(e.toString());
		}
		logger.info("===========");
		return new DashboardCounters(ac.get(ORGANIZATION_COUNT),
				ac.get(APPLICATION_COUNT), ac.get(ADMIN_USER_COUNT));
	}

	@Autowired
	public void setEntityManagerFactory(EntityManagerFactory emf) {
		this.emf = emf;
	}

}
