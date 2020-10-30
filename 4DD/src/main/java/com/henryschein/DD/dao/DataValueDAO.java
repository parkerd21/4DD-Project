package com.henryschein.DD.dao;

import com.henryschein.DD.entity.DataValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataValueDAO extends JpaRepository<DataValue, Integer> {

}
