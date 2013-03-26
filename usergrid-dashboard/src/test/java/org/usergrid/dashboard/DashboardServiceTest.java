package org.usergrid.dashboard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.usergrid.management.ManagementService;
import org.usergrid.management.ManagementTestHelper;
import org.usergrid.management.OrganizationOwnerInfo;
import org.usergrid.management.cassandra.ManagementTestHelperImpl;

public class DashboardServiceTest {
	private static final Logger logger = LoggerFactory
			.getLogger(DashboardServiceTest.class);
	static ManagementTestHelper helper;
	static ManagementService management;
	static DashboardService dashboardService;

	@BeforeClass
	public static void setup() throws Exception {
		logger.info("setup");
		assertNull(helper);
		helper = new ManagementTestHelperImpl();
		// helper.setClient(this);
		helper.setup();
		management = helper.getManagementService();
		dashboardService = helper.getApplicationContext().getBean(
				DashboardService.class);
	}

	@AfterClass
	public static void teardown() throws Exception {
		logger.info("teardown");
		helper.teardown();
	}

	private static int uniqueCounter;

	private String getUniqueEmail() {
		return getUniqueString() + "@" + getUniqueString() + ".com";
	}

	private String getUniqueString() {
		uniqueCounter++;
		return "dashboardtest" + uniqueCounter;
	}

	@Test
	public void countAdminUserTest() throws Exception {
		long initialAdminUserCount = dashboardService.getDashboardCounters()
				.getAdminUserCount();
		management.createAdminUser(getUniqueString(), getUniqueString(),
				getUniqueEmail(), getUniqueString(), true, false);
		Thread.sleep(1000);
		DashboardCounters dashboardCounters = dashboardService
				.getDashboardCounters();
		assertEquals(1 + initialAdminUserCount,
				dashboardCounters.getAdminUserCount());
		logger.info(dashboardCounters.toString());
	}

	@Test
	public void countApplicationTest() throws Exception {
		long initialApplicationUserCount = dashboardService
				.getDashboardCounters().getApplicationCount();
		OrganizationOwnerInfo organization = management
				.createOwnerAndOrganization(getUniqueString(),
						getUniqueString(), getUniqueString(), getUniqueEmail(),
						getUniqueString());
		
		management.createApplication(organization.getOrganization().getUuid(),
				getUniqueString());
		Thread.sleep(1000);
		DashboardCounters dashboardCounters = dashboardService
				.getDashboardCounters();
		assertEquals(1 + initialApplicationUserCount,
				dashboardCounters.getApplicationCount());
	}

	@Test
	public void countOrganizationTest() throws Exception {
		long initialOrganizationCount = dashboardService.getDashboardCounters()
				.getOrganizationCount();
		String uniqueString = getUniqueString();
		System.out.println(uniqueString);
		management.createOwnerAndOrganization(uniqueString, getUniqueString(),
				getUniqueString(), getUniqueEmail(), getUniqueString());
		DashboardCounters dashboardCounters = dashboardService
				.getDashboardCounters();
		assertEquals(1 + initialOrganizationCount,
				dashboardCounters.getOrganizationCount());
	}

}
