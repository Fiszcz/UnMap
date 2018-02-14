package com.youmap.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.youmap.entity.Travel;


@Repository
public interface TravelDAO extends CrudRepository<Travel, Long> {
    Travel findByCode(String code);
}
