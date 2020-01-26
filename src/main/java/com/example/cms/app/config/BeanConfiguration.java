package com.example.cms.app.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;

import javax.persistence.EntityManager;

@Configuration
public class BeanConfiguration {
	
	@Bean
	public RoleHierarchy roleHierarchy(){
	    RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
	    roleHierarchy.setHierarchy("ADMIN > MECHANIC ADMIN > OFFICE_WORKER ADMIN > OWNER");
	    return roleHierarchy;
	}


	@Bean
	public JPAQueryFactory jpaQueryFactory(EntityManager entityManager){
		return new JPAQueryFactory(entityManager);
	}

}
