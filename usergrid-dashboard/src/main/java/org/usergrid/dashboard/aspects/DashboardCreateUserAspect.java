package org.usergrid.dashboard.aspects;

import org.usergrid.dashboard.service.DashboardService;
import java.util.UUID;


import org.aspectj.lang.ProceedingJoinPoint;
import org.usergrid.management.ApplicationInfo;
import org.usergrid.persistence.EntityManager;
import org.usergrid.persistence.Schema;
import org.usergrid.persistence.entities.Application;
import org.usergrid.persistence.entities.User;

public class DashboardCreateUserAspect {

    private DashboardService dashboardService;

    public void setDashboardService(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    public Object applyTrace(ProceedingJoinPoint pjp) throws Throwable {
        boolean user = false;
        boolean success = false;

        if (pjp.getArgs()[0] instanceof String
                && pjp.getArgs()[0].equals(Schema.getDefaultSchema()
                .getEntityType(User.class))) {
            user = true;
        } else if (pjp.getArgs()[0] instanceof User) {
            user = true;
        } else if (pjp.getArgs()[0] instanceof UUID
                && pjp.getArgs()[1].equals(Schema.getDefaultSchema()
                .getEntityType(User.class))) {
            user = true;
        }

        try {
            return pjp.proceed();
        } catch (Exception e) {
            success = false;
            throw e;
        } finally {
            if (success && user) {
                EntityManager em = (EntityManager) pjp.getTarget();
                Application application = em.getApplication();
                ApplicationInfo info = new ApplicationInfo(
                        application.getUuid(), application.getName());
                dashboardService.appUserCreated(info);
            }
        }
    }
}
