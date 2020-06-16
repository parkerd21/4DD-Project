package com.henryschein.DD.service;

import com.henryschein.DD.dao.DataElementRepository;
import com.henryschein.DD.entity.DataElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DataElementService {

    private DataElementRepository dataElementRepository;

    @Autowired
    public DataElementService(DataElementRepository theDataElementRepository) {
        dataElementRepository = theDataElementRepository;
    }

    public Optional<DataElement> findByCoords(Integer x, Integer y) {
        return dataElementRepository.findByCoords(x, y);
    }

    public Optional<DataElement> findById(Long id) {
        return dataElementRepository.findById(id);
    }

    public List<DataElement> findAll() {
        return dataElementRepository.findAll();
    }

    public void save(DataElement dataElement) {
        dataElementRepository.save(dataElement);
    }

    public void deleteById(Long id) {
        dataElementRepository.deleteById(id);
    }

}
