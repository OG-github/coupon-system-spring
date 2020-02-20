package com.og.CouponSystemJB.service;

/*----------------- IMPORTS -----------------------------------------------------------------------------------------*/

/*-------------------- springframework --------------------*/

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;


/**
 * Used b clientsession to hold all services. clientsession is set by loginservice to the respective service it needs
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public interface EntityService {
}
