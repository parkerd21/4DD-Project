package com.henryschein.DD.controller;

import com.henryschein.DD.entity.DataElement;
import com.henryschein.DD.service.DataElementService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("data_elements")
public class DataElementController {

    private DataElementService dataElementService;

    public DataElementController(DataElementService theDataElementService) {
        this.dataElementService = theDataElementService;
    }

    // TODO: Create relationships with dataElement and Page and Book
    // TODO: update the endpoints to CRUD a dataElement from a Page in a Book


    @GetMapping("/{id}")
    public Optional<DataElement> getById(@PathVariable Long id) {
        return  dataElementService.findById(id);
    }

    @GetMapping("/ByCoords")
    public Optional<DataElement> getByCoords(@RequestParam Integer x, @RequestParam Integer y) {
        return dataElementService.findByCoords(x, y);
    }

    @GetMapping("/")
    public List<DataElement> getAll() {
        return dataElementService.findAll();
    }

    @PostMapping("/")
    public DataElement addDataElement(@RequestBody DataElement dataElement) {
        dataElement.setData_id(0L);
        dataElementService.save(dataElement);
        return  dataElement;
    }

    @PutMapping("/")
    public DataElement updateDataElement(@RequestBody DataElement dataElement) {
        dataElementService.save(dataElement);
        return dataElement;
    }

    @DeleteMapping("/{id}")
    public String deleteDataElement(@PathVariable Long id) {
        Optional<DataElement> dataElement = dataElementService.findById(id);
        if (dataElement.isEmpty()) {
            throw new RuntimeException("DataElement not found");
        }
        dataElementService.deleteById(id);
        return "Deleted dataElement with id " + id;
    }

    //create CRUD endpoints
}
