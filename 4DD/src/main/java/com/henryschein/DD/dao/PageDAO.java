package com.henryschein.DD.dao;

import com.henryschein.DD.entity.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PageDAO extends JpaRepository<Page, Integer> {

    @Query("SELECT p FROM Page p where p.pageId = :pageId")
    Page getById(@Param("pageId") Integer pageId);
}
