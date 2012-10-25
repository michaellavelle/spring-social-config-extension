package org.springframework.social.extension.config.annotation;

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

import java.util.Map;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.social.extension.config.support.JpaConnectionRepositoryConfigSupport;

/**
 * {@link ImportBeanDefinitionRegistrar} to enable {@link EnableJpaConnectionRepository} annotation.
 * @author Michael Lavelle
 */
class JpaConnectionRepositoryRegistrar extends JpaConnectionRepositoryConfigSupport implements ImportBeanDefinitionRegistrar {

	public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
		Map<String, Object> annotationAttributes = annotationMetadata.getAnnotationAttributes(EnableJpaConnectionRepository.class.getName());
		if (annotationAttributes == null) {
			return;
		}
		AnnotationAttributes attributes = new AnnotationAttributes(annotationAttributes);
		String connectionRepositoryId = attributes.getString("connectionRepositoryId");
		String usersConnectionRepositoryId = attributes.getString("usersConnectionRepositoryId");
		String connectionFactoryLocatorRef = attributes.getString("connectionFactoryLocatorRef");
		String jpaTemplateRef = attributes.getString("jpaTemplateRef");
		String encryptorRef = attributes.getString("encryptorRef");
		String userIdSourceRef = attributes.getString("userIdSourceRef");
		registerJpaConnectionRepositoryBeans(registry, connectionRepositoryId, usersConnectionRepositoryId, connectionFactoryLocatorRef, jpaTemplateRef, encryptorRef, userIdSourceRef);
	}

}
