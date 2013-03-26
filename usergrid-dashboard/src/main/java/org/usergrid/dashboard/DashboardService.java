package org.usergrid.dashboard;

import org.usergrid.management.ApplicationInfo;
import org.usergrid.management.OrganizationInfo;
import org.usergrid.management.OrganizationOwnerInfo;
import org.usergrid.management.UserInfo;

public interface DashboardService {

	void applicationCreated(ApplicationInfo applicationInfo);

	void organizationCreated(OrganizationInfo organizationInfo);

	void organizationOwnerCreated(OrganizationOwnerInfo organizationOwnerInfo);

	void adminUserCreated(UserInfo userInfo);

	DashboardCounters getDashboardCounters() throws Exception;

}
