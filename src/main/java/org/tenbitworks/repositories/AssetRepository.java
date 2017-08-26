package org.tenbitworks.repositories;


import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.tenbitworks.model.Asset;
import org.tenbitworks.model.Member;

public interface AssetRepository extends CrudRepository<Asset, Long> {
	List<Asset> findAllByMembers(Member member);
}
