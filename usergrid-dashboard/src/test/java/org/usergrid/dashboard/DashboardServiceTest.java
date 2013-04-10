package org.usergrid.dashboard;

import java.util.List;
import org.usergrid.dashboard.service.DashboardService;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.usergrid.dashboard.domain.UsergridCounter;
import org.usergrid.management.ManagementService;
import org.usergrid.management.ManagementTestHelper;
import org.usergrid.management.OrganizationOwnerInfo;
import org.usergrid.management.cassandra.ManagementTestHelperImpl;
import org.usergrid.persistence.TypedEntity;
import org.usergrid.persistence.entities.User;

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

    private long getDashboardCounter(String name) {
        List<UsergridCounter> dashboardCounters = dashboardService.getDashboardCounters();
        for (UsergridCounter usergridCounter : dashboardCounters) {
            if (usergridCounter.getName().equals(name)) {
                return usergridCounter.getCounter();
            }
        }
        return 0L;
    }

    @Test
    public void countAdminUserTest() throws Exception {
        Long initialAdminUserCount = getDashboardCounter(DashboardService.ADMINUSER_COUNTER);
        management.createAdminUser(getUniqueString(), getUniqueString(),
                getUniqueEmail(), getUniqueString(), true, false);
        assertEquals(1 + initialAdminUserCount,
                getDashboardCounter(DashboardService.ADMINUSER_COUNTER));
        logger.info(dashboardService.getDashboardCounters().toString());
    }

    @Test
    public void countApplicationTest() throws Exception {
        long initialApplicationUserCount = getDashboardCounter(DashboardService.APPLICATIONS_COUNTER);
        OrganizationOwnerInfo organization = management
                .createOwnerAndOrganization(getUniqueString(),
                getUniqueString(), getUniqueString(), getUniqueEmail(),
                getUniqueString());

        management.createApplication(organization.getOrganization().getUuid(),
                getUniqueString());

        assertEquals(1 + initialApplicationUserCount,
                getDashboardCounter(DashboardService.APPLICATIONS_COUNTER));
    }

    @Test
    public void countOrganizationTest() throws Exception {
        long initialOrganizationCount = getDashboardCounter(DashboardService.ORGANIZATIONS_COUNTER);
        String uniqueString = getUniqueString();
        System.out.println(uniqueString);
        management.createOwnerAndOrganization(uniqueString, getUniqueString(),
                getUniqueString(), getUniqueEmail(), getUniqueString());
        assertEquals(1 + initialOrganizationCount,
                getDashboardCounter(DashboardService.ORGANIZATIONS_COUNTER));
    }

    @Test
    public void testUserAssign() {
        assertTrue(TypedEntity.class.isAssignableFrom(User.class));
    }
}
