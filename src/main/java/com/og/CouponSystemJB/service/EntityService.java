package com.og.CouponSystemJB.service;

/*----------------- IMPORTS -----------------------------------------------------------------------------------------*/

/*-------------------- springframework --------------------*/

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;


/**
 * This will be a sort of an "abstract" interface in which all Services for different Entities will inherit from.
 * This is done to create hierarchy in order to use polymorphism in the class ClientSession. ClientSession will hold
 * a general super class EntityService member field in which LoginService will set to the respective necessary
 * Service sub class when login is performed. After the LoginService sets the EntityService for ClientSession,
 * ClientSession will delegate the requests made by the REST controller to its EntityService field member.
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public interface EntityService {
}
