package com.henryschein.DD.service;

import com.henryschein.DD.dao.DataElementDAO;
import com.henryschein.DD.dto.DataElementDTO;
import com.henryschein.DD.entity.DataElement;
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

    public List<DataElement> findAll() {
        return dataElementDAO.findAll();
    }

    public String createAndAdd(DataElementDTO dataElementDTO) {
        DataElement currentElement =
                getByXY(dataElementDTO.getPageId(), dataElementDTO.getXcoord(), dataElementDTO.getYcoord());
        if (currentElement == null) {
            dataElementDTO.setZcoord(1);
            DataElement newElement = new DataElement(dataElementDTO);
            dataElementDAO.saveAndFlush(newElement);
            return newElement.toString();
        }
        else
            return "Cannot create a new dataElement at that location because one already exists there." +
                    " Try updating or a new location";
    }

    public String update(DataElementDTO dataElementDTO) {
        DataElement currentElement =
                getByXY(dataElementDTO.getPageId(), dataElementDTO.getXcoord(), dataElementDTO.getYcoord());
        DataElement newElement;

        if (currentElement != null) {
            newElement = new DataElement(dataElementDTO);
            newElement.setZcoord(currentElement.getZcoord() + 1);
            dataElementDAO.saveAndFlush(newElement);
            return newElement.toString();
        }
        return "No element exists at that location to update";
    }

    public List<DataElement> getHistory(Long pageId, Integer x, Integer y) {
        return dataElementDAO.getHistory(pageId, x, y);
    }

    @Transactional
    public void deleteByXY(Long pageId, Integer x, Integer y) {
        dataElementDAO.deleteByXY(pageId, x, y);
    }
}
