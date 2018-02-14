package com.youmap.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.youmap.entity.Point;


@Repository
public interface PointDAO extends CrudRepository<Point, Long> {
    Point findByName(String name);
    long deleteByName(String name);
}
