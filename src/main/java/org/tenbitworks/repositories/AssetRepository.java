package org.tenbitworks.repositories;


import org.springframework.data.repository.CrudRepository;
import org.tenbitworks.model.Asset;

public interface AssetRepository extends CrudRepository<Asset, Long> {

}
