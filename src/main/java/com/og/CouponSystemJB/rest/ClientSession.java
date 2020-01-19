package com.og.CouponSystemJB.rest;

import com.og.CouponSystemJB.service.EntityService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ClientSession {

    private EntityService service;

    private long lastAccessedMillis;

    public EntityService getService() {
        this.accessed();
        return service;
    }

    public void setService(EntityService service) {
        this.service = service;
    }

    public long getLastAccessedMillis() {
        return lastAccessedMillis;
    }

    public void accessed() {
        this.lastAccessedMillis = System.currentTimeMillis();
    }


}

