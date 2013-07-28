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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

/**
 * Annotation to enable Roo-based persistence of connections.
 * Configures a singleton {@link RooUsersConnectionRepository} and a request-scoped RooConnectionRepository. 
 * @author Michael Lavelle
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(RooConnectionRepositoryRegistrar.class)
public @interface EnableRooConnectionRepository {

	/**
	 * The ID to assign to the ConnectionRepository bean.
	 * Defaults to "connectionRepository". 
	 */
	String connectionRepositoryId() default "connectionRepository";
	
	/**
	 * The ID to assign to the UsersConnectionRepository bean.
	 * Defaults to "usersConnectionRepository". 
	 */
	String usersConnectionRepositoryId() default "usersConnectionRepository";
	
	/**
	 * The ID of the ConnectionFactoryLocator bean to fetch a ConnectionFactory from when creating/persisting connections.
	 * Defaults to "connectionFactoryLocator". 
	 */
	String connectionFactoryLocatorRef() default "connectionFactoryLocator";
	
	/**
	 * The ID of a RooTemplate for accessing the database.
	 * Defaults to "rooUserConnectionTemplate". 
	 */
	String rooTemplateRef() default "rooUserConnectionTemplate";
	
	/**
	 * The ID of a TextEncryptor used when persisting connection details.
	 * Defaults to "textEncryptor". 
	 */
	String encryptorRef() default "textEncryptor";
	
	/**
	 * The ID of a UserIdSource bean used to determine the unique identifier of the current user.
	 * Defaults to "userIdSource". 
	 */
	String userIdSourceRef() default "userIdSource";
	
	/**
	* Reference to {@link ConnectionSignUp} bean to execute to create a new local user profile in the event no user id could be mapped to a connection.
	* Allows for implicitly creating a user profile from connection data during a provider sign-in attempt.
	* Defaults to null, indicating explicit sign-up will be required to complete the provider sign-in attempt.
	*/
	String connectionSignUpRef() default "";
	
}
