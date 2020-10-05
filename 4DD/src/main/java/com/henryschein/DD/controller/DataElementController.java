package com.henryschein.DD.controller;

import com.henryschein.DD.dto.DataElementDTO;
import com.henryschein.DD.entity.DataElement;
import com.henryschein.DD.service.DataElementService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("data_elements")
public class DataElementController {

    private DataElementService dataElementService;

    public DataElementController(DataElementService theDataElementService) {
        this.dataElementService = theDataElementService;
    }

    @GetMapping("/")
    public DataElement getByCoords(
            @RequestParam Integer pageId, @RequestParam Integer x, @RequestParam Integer y,
            @RequestParam(value = "z", required = false) Integer z) {
        return dataElementService.getDataElement(new DataElementDTO(pageId, x, y, z));
    }

    @GetMapping("/test")
    public Double testEndPoint(@RequestParam Integer pageId, @RequestBody String equation) {
        return dataElementService.evaluateEquationString(pageId, equation);
    }

    @GetMapping("/history")
    public List<DataElement> getHistory(@RequestParam Integer pageId, @RequestParam Integer x, @RequestParam Integer y) {
        return dataElementService.getDataElementListByXY(new DataElementDTO(pageId, x, y));
    }

    @GetMapping("/all")
    public List<DataElement> getAll() {
        return dataElementService.getAll();
    }

    @GetMapping("/row")
    public List<DataElement> getByRow(@RequestParam Integer pageId, @RequestParam Integer rowNumber) {
        return dataElementService.getByRow(pageId, rowNumber);
    }

    @GetMapping("/column")
    public List<DataElement> getByColumn(@RequestParam Integer pageId, @RequestParam Integer columnNumber) {
        return dataElementService.getByColumn(pageId, columnNumber);
    }

    @GetMapping("/range")
    public List<DataElement> getByRange(@RequestParam Integer pageId, @RequestParam String range) {
        return dataElementService.getByRange(pageId, range);
    }

    @PostMapping("/")
    public DataElement createNewDataElement(@RequestBody DataElementDTO dataElementDTO) {
        dataElementDTO.setDataId(null);
        dataElementDTO.setZcoord(null);
        return dataElementService.createNewDataElement(dataElementDTO);
    }

    @PutMapping("/")
    public DataElement update(@RequestBody DataElementDTO dataElementDTO) {
        dataElementDTO.setDataId(null);
        dataElementDTO.setZcoord(null);
        return dataElementService.updateDataElement(dataElementDTO);
    }

    @DeleteMapping("/")
    public void deleteByXY(@RequestParam Integer pageId, @RequestParam Integer x, @RequestParam Integer y) {
        dataElementService.deleteByXY(new DataElementDTO(pageId, x, y));
    }
}
