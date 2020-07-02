package com.henryschein.DD.dao;

import com.henryschein.DD.entity.DataElement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataElementDAO extends JpaRepository<DataElement, Long> {

    @Query("SELECT d FROM DataElement d WHERE d.xcoord = :x AND d.ycoord = :y AND d.zcoord = :z AND d.pageId = :pageId")
    DataElement getByXYZ(
            @Param("pageId") Long pageId, @Param("x") Integer x, @Param("y") Integer y, @Param("z") Integer z
    );

    @Query(nativeQuery = true,
            value = "SELECT TOP 1 * FROM DATA_ELEMENT d WHERE d.xcoord = :x AND d.ycoord = :y AND d.page_id = :pageId " +
            "ORDER BY d.zcoord DESC")
    DataElement getByXY(@Param("pageId") Long pageId, @Param("x") Integer x, @Param("y") Integer y);

    @Query("SELECT d FROM DataElement d WHERE d.xcoord = :x AND d.ycoord = :y AND d.pageId = :pageId")
    List<DataElement> getHistory(@Param("pageId") Long pageId, @Param("x") Integer x, @Param("y") Integer y);

    @Modifying
    @Query("DELETE FROM DataElement d WHERE d.xcoord = :x AND d.ycoord = :y AND d.pageId = :pageId")
    void deleteByXY(@Param("pageId") Long pageId, @Param("x") Integer x, @Param("y") Integer y);
}
