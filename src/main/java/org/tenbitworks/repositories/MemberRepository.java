package org.tenbitworks.repositories;


import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.tenbitworks.model.Member;

public interface MemberRepository extends CrudRepository<Member, UUID> {

}
