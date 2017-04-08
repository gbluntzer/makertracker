package org.tenbitworks.repositories;


import org.springframework.data.repository.CrudRepository;
import org.tenbitworks.model.Member;

public interface MemberRepository extends CrudRepository<Member, Long> {

}
