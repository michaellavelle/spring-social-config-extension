package org.springframework.social.extension.config.support;

/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.social.connect.roo.RooUserConnectionTemplate;
import org.springframework.social.connect.roo.RooUsersConnectionRepository;

/**
 * Support class providing methods for configuring a singleton {@link RooUsersConnectionRepository} bean and a request-scoped RooConnectionRepository bean.
 * Used by RooConnectionRepositoryRegistrar (for EnableRooConnectionRepository) 
 * @author Michael Lavelle
 */
public abstract class RooConnectionRepositoryConfigSupport {

	private final static Log logger = LogFactory.getLog(RooConnectionRepositoryConfigSupport.class);

	public BeanDefinition registerRooConnectionRepositoryBeans(BeanDefinitionRegistry registry, String connectionRepositoryId, String usersConnectionRepositoryId, 
			String connectionFactoryLocatorRef, String rooTemplateRef, String encryptorRef, String userIdSourceRef) {
		registerUsersConnectionRepositoryBeanDefinition(registry, usersConnectionRepositoryId, connectionFactoryLocatorRef, rooTemplateRef, encryptorRef);
		registerUserIdBeanDefinition(registry, userIdSourceRef);
		registerDefaultRooTemplateIfNotRegistered(registry,rooTemplateRef);
		return registerConnectionRepository(registry, usersConnectionRepositoryId, connectionRepositoryId);		
	}
	
	
	private BeanDefinition registerUsersConnectionRepositoryBeanDefinition(BeanDefinitionRegistry registry, String usersConnectionRepositoryId, 
			String connectionFactoryLocatorRef, String rooTemplateRef, String encryptorRef) {
		if (logger.isDebugEnabled()) {
			logger.debug("Registering RooUsersConnectionRepository bean");
		}				

		BeanDefinition usersConnectionRepositoryBD = BeanDefinitionBuilder.genericBeanDefinition(RooUsersConnectionRepository.class)
				.addConstructorArgReference(rooTemplateRef)
				.addConstructorArgReference(connectionFactoryLocatorRef)
				.addConstructorArgReference(encryptorRef)
				.getBeanDefinition();
		BeanDefinition scopedProxyBean = decorateWithScopedProxy(usersConnectionRepositoryId, usersConnectionRepositoryBD, registry);
		registry.registerBeanDefinition(usersConnectionRepositoryId, scopedProxyBean);
		return scopedProxyBean;
	}
	
	// TODO: Kinda hackish...pushes a request-scoped String containing the name retrieved from the UserIdSource into the context.
	private BeanDefinition registerUserIdBeanDefinition(BeanDefinitionRegistry registry, String userIdSourceRef) {
		BeanDefinition userIdStringDB = BeanDefinitionBuilder.genericBeanDefinition().getBeanDefinition();
		userIdStringDB.setFactoryBeanName(userIdSourceRef);
		userIdStringDB.setFactoryMethodName("getUserId");
		userIdStringDB.setScope("request");
		registry.registerBeanDefinition(USER_ID_STRING_ID, userIdStringDB);
		return userIdStringDB;
	}
	
	// Need to check if already registered, as component scanning may have imported the default RooTemplate
	// from spring-social-roo-connectionrepository
	private BeanDefinition registerDefaultRooTemplateIfNotRegistered(BeanDefinitionRegistry registry, String rooTemplateRef)
	{
		if (!registry.containsBeanDefinition(rooTemplateRef))
		{
			BeanDefinition rooTemplateDB = BeanDefinitionBuilder.genericBeanDefinition(RooUserConnectionTemplate.class).getBeanDefinition();
			registry.registerBeanDefinition(rooTemplateRef, rooTemplateDB);
		}
		return null;
	}

	private BeanDefinition registerConnectionRepository(BeanDefinitionRegistry registry, String usersConnectionRepositoryId, String connectionRepositoryId) {
		if (logger.isDebugEnabled()) {
			logger.debug("Registering RooConnectionRepository bean");
		}		
		BeanDefinition connectionRepositoryBD = BeanDefinitionBuilder.genericBeanDefinition().addConstructorArgReference(USER_ID_STRING_ID).getBeanDefinition();
		connectionRepositoryBD.setFactoryBeanName(usersConnectionRepositoryId);
		connectionRepositoryBD.setFactoryMethodName(CREATE_CONNECTION_REPOSITORY_METHOD_NAME);
		connectionRepositoryBD.setScope("request");
		registry.registerBeanDefinition(connectionRepositoryId, decorateWithScopedProxy(connectionRepositoryId, connectionRepositoryBD, registry));
		return connectionRepositoryBD;
	}

	private BeanDefinition decorateWithScopedProxy(String beanName, BeanDefinition beanDefinition, BeanDefinitionRegistry registry) {
		BeanDefinitionHolder beanDefinitionHolder = new BeanDefinitionHolder(beanDefinition, beanName);
		return ScopedProxyUtils.createScopedProxy(beanDefinitionHolder, registry, false).getBeanDefinition();
	}

	private static final String USER_ID_STRING_ID = "__userIdString";

	private static final String CREATE_CONNECTION_REPOSITORY_METHOD_NAME = "createConnectionRepository";

}
