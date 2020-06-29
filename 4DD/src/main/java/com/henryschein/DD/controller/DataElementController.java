package com.henryschein.DD.controller;

import com.henryschein.DD.dto.DataElementDTO;
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

@GetMapping("/")
    public Optional<DataElement> getByCoords(
            @RequestParam Long pageId, @RequestParam Integer x, @RequestParam Integer y, @RequestParam(value = "z", required = false) Integer z)
    {
        if (z == null)
            return dataElementService.getByXY(pageId, x, y);
        else
            return dataElementService.getByXYZ(pageId, x, y, z);
    }

    @GetMapping("/history")
    public List<DataElement> getHistory(@RequestParam Long pageId, @RequestParam Integer x, @RequestParam Integer y) {
        return dataElementService.getHistory(pageId, x, y);
    }

    @GetMapping("/all")
    public List<DataElement> getAll() {
        return dataElementService.findAll();
    }

    @PostMapping("/")
    public DataElement addDataElement(@RequestBody DataElementDTO dataElementDTO) {
        dataElementDTO.setDataId(0L);
        return dataElementService.createAndAdd(dataElementDTO);
    }

    @PutMapping("/")
    public DataElement updateDataElement(@RequestBody DataElementDTO dataElementDTO) {
        // TODO: Add logic so that when a element is updated it has the history of the previous data
        return dataElementService.update(dataElementDTO);
    }

//    @DeleteMapping("/{id}")
//    public String deleteDataElement(@PathVariable Long id) {
//        Optional<DataElement> dataElement = dataElementService.findById(id);
//        if (dataElement.isEmpty()) {
//            throw new RuntimeException("DataElement not found");
//        }
//        dataElementService.deleteById(id);
//        return "Deleted dataElement with id " + id;
//    }
}
