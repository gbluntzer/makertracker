package org.tenbitworks.repositories;


import org.springframework.data.repository.PagingAndSortingRepository;
import org.tenbitworks.model.News;


public interface NewsRepository extends PagingAndSortingRepository<News, Long> {

}
