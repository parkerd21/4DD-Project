package com.henryschein.DD.service;

import com.henryschein.DD.dao.DataElementDAO;
import com.henryschein.DD.dto.DataElementDTO;
import com.henryschein.DD.entity.DataElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DataElementService {

    private DataElementDAO dataElementDAO;

    @Autowired
    public DataElementService(DataElementDAO theDataElementDAO) {
        dataElementDAO = theDataElementDAO;
    }

    public Optional<DataElement> getByXYZ(Long pageId, Integer x, Integer y, Integer z) {
        return dataElementDAO.getByXYZ(pageId, x, y, z);
    }

    public Optional<DataElement> getByXY(Long pageId, Integer x, Integer y) {
        return dataElementDAO.getByXY(pageId, x, y);
    }

    public List<DataElement> findAll() {
        return dataElementDAO.findAll();
    }

    public DataElement createAndAdd(DataElementDTO dataElementDTO) {
        Optional<DataElement> currentDataElement = getByXY(dataElementDTO.getPageId(), dataElementDTO.getXcoord(), dataElementDTO.getYcoord());
        if (currentDataElement.isEmpty()) {
            dataElementDTO.setZcoord(1);
            DataElement dataElement = new DataElement(dataElementDTO);
            return dataElementDAO.saveAndFlush(dataElement);
        }
        else
            return null; // throw exception
    }

    public DataElement update(DataElementDTO dataElementDTO) {
        DataElement dataElement = new DataElement(dataElementDTO);
        return dataElementDAO.saveAndFlush(dataElement);
    }

    public List<DataElement> getHistory(Long pageId, Integer x, Integer y) {
        return dataElementDAO.getHistory(pageId, x, y);
    }
}
