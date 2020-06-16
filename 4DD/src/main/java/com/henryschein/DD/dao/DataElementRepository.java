package com.henryschein.DD.dao;

import com.henryschein.DD.entity.DataElement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface DataElementRepository extends JpaRepository<DataElement, Long> {

    @Query("SELECT d FROM DataElement d WHERE d.xcoord = :x AND d.ycoord = :y")
    Optional<DataElement> findByCoords(@Param("x") Integer x, @Param("y") Integer y);

}
