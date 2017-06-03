package org.tenbitworks.configuration;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.tenbitworks.model.Member;
import org.tenbitworks.repositories.MemberRepository;

@Configuration
public class CustomPermissionEvaluator implements PermissionEvaluator {
	@Autowired
	MemberRepository memberRepository;

	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
		System.out.println(authentication + " " + targetDomainObject + " " + permission);

		if ((authentication == null) || (targetDomainObject == null) || !(permission instanceof String)){
			return false;
		}
//        String targetType = targetDomainObject.getClass().getSimpleName().toUpperCase();
//        return hasPrivilege(authentication, targetType, permission.toString().toUpperCase());
		//TODO Implement this
		return false;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
		System.out.println(authentication + " " + targetId + " " + targetType + " " + permission);

		if ((authentication == null) || (targetType == null) || !(permission instanceof String)) {
			return false;
		}
		
		if (targetId instanceof UUID && targetType.equalsIgnoreCase("Member")) {
			Member member = memberRepository.findOne((UUID) targetId);
			if (member != null && member.getUser() != null) {
				return member.getUser().getUsername().equals(authentication.getName());
			}
		}
		return false;
	}
}
