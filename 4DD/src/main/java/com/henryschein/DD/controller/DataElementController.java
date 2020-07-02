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
            @RequestParam Long pageId, @RequestParam Integer x, @RequestParam Integer y,
            @RequestParam(value = "z", required = false) Integer z)
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
        return dataElementService.getAll();
    }

    @PostMapping("/")
    public String add(@RequestBody DataElementDTO dataElementDTO) {
        dataElementDTO.setDataId(null);
        dataElementDTO.setZcoord(null);
        return dataElementService.add(dataElementDTO);
    }

    @PutMapping("/")
    public String update(@RequestBody DataElementDTO dataElementDTO) {
        dataElementDTO.setDataId(null);
        dataElementDTO.setZcoord(null);
        return dataElementService.update(dataElementDTO);
    }

    @DeleteMapping("/")
    public String deleteByXY(@RequestParam Long pageId, @RequestParam Integer x, @RequestParam Integer y) {
        dataElementService.deleteByXY(pageId, x, y);
        return "Deleted dataElement and its history at pageId: " + pageId + ", xcoord: " + x + ", ycoord: " + y;
    }
}
