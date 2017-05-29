package org.tenbitworks.repositories;


import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.tenbitworks.model.Member;
import org.tenbitworks.model.User;

public interface MemberRepository extends PagingAndSortingRepository<Member, UUID> {
	Member findOneByUser(User user);
	List<Member> findAllByUser(User user);
}
