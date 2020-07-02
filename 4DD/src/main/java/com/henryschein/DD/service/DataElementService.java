package com.henryschein.DD.service;

import com.henryschein.DD.dao.DataElementDAO;
import com.henryschein.DD.dto.DataElementDTO;
import com.henryschein.DD.entity.DataElement;
import com.henryschein.DD.entity.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class DataElementService {

    private DataElementDAO dataElementDAO;

    @Autowired
    public DataElementService(DataElementDAO theDataElementDAO) {
        dataElementDAO = theDataElementDAO;
    }

    public DataElement getByXYZ(Long pageId, Integer x, Integer y, Integer z) {
        return dataElementDAO.getByXYZ(pageId, x, y, z);
    }

    public DataElement getByXY(Long pageId, Integer x, Integer y) {
        return dataElementDAO.getByXY(pageId, x, y);
    }

    public List<DataElement> getAll() {
        return dataElementDAO.findAll();
    }

    public DataElement add(DataElementDTO dataElementDTO) {
        DataElement initialElement =
                getByXY(dataElementDTO.getPageId(), dataElementDTO.getXcoord(), dataElementDTO.getYcoord());
        if (initialElement == null) {
            dataElementDTO.setZcoord(1);
            DataElement newElement = new DataElement(dataElementDTO);
            return dataElementDAO.saveAndFlush(newElement);
        }
        else
            return null;
    }

    public DataElement update(DataElementDTO dataElementDTO) {
        DataElement initialElement =
                getByXY(dataElementDTO.getPageId(), dataElementDTO.getXcoord(), dataElementDTO.getYcoord());
        if (initialElement != null) {
            DataElement newElement = new DataElement(dataElementDTO);
            newElement.setZcoord(initialElement.getZcoord() + 1);
            return dataElementDAO.saveAndFlush(newElement);
        }
        else
            return null;
    }

    public List<DataElement> getHistory(Long pageId, Integer x, Integer y) {
        return dataElementDAO.getHistory(pageId, x, y);
    }

    @Transactional
    public String deleteByXY(Long pageId, Integer x, Integer y) {
        dataElementDAO.deleteByXY(pageId, x, y);
        return "Deleted dataElement and its history at pageId: " + pageId + ", xcoord: " + x + ", ycoord: " + y;
    }

    public boolean dataElementExists(Long pageId, Integer x, Integer y) {
        DataElement dataElement = dataElementDAO.getByXY(pageId, x, y);
        return dataElement != null;
    }
}
