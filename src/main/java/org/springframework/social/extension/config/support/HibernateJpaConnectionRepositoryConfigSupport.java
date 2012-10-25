package org.springframework.social.extension.config.support;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.social.connect.jpa.hibernate.UserConnectionDao;

public abstract class HibernateJpaConnectionRepositoryConfigSupport extends
		JpaConnectionRepositoryConfigSupport {

	@Override
	public BeanDefinition registerJpaConnectionRepositoryBeans(
			BeanDefinitionRegistry registry, String connectionRepositoryId,
			String usersConnectionRepositoryId,
			String connectionFactoryLocatorRef, String userConnectionDaoRef,
			String encryptorRef, String userIdSourceRef) {
		
		registerDefaultUserConnectionDaoIfNotRegistered(registry,userConnectionDaoRef);
		return super.registerJpaConnectionRepositoryBeans(registry,
				connectionRepositoryId, usersConnectionRepositoryId,
				connectionFactoryLocatorRef, userConnectionDaoRef, encryptorRef,
				userIdSourceRef);
	}

	// Need to check if already registered, as component scanning may have imported the default RooTemplate
	// from spring-social-roo-connectionrepository
	private BeanDefinition registerDefaultUserConnectionDaoIfNotRegistered(BeanDefinitionRegistry registry, String userConnectionDaoRef)
	{
		if (!registry.containsBeanDefinition(userConnectionDaoRef))
		{
			BeanDefinition jpaTemplateDB = BeanDefinitionBuilder.genericBeanDefinition(UserConnectionDao.class).getBeanDefinition();
			registry.registerBeanDefinition(userConnectionDaoRef, jpaTemplateDB);
		}
		return null;
	}
	
}
