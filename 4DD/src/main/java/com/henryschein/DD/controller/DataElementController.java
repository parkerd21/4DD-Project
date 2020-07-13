package com.henryschein.DD.controller;

import com.henryschein.DD.dto.DataElementDTO;
import com.henryschein.DD.entity.DataElement;
import com.henryschein.DD.service.DataElementService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

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
        if (Objects.isNull(z)) {
            return dataElementService.getByXY(new DataElementDTO(pageId, x, y));
        }
        else {
            return dataElementService.getByXYZ(new DataElementDTO(pageId, x, y, z));
        }
    }

    @GetMapping("/history")
    public List<DataElement> getHistory(@RequestParam Long pageId, @RequestParam Integer x, @RequestParam Integer y) {
        return dataElementService.getHistory(new DataElementDTO(pageId, x, y));
    }

    @GetMapping("/all")
    public List<DataElement> getAll() {
        return dataElementService.getAll();
    }

    @PostMapping("/")
    public DataElement createNewDataElement(@RequestBody DataElementDTO dataElementDTO) {
        dataElementDTO.setDataId(null);
        //dataElementDTO.setZcoord(null);
        return dataElementService.createNewDataElement(dataElementDTO);
    }

    @PutMapping("/")
    public DataElement update(@RequestBody DataElementDTO dataElementDTO) {
        dataElementDTO.setDataId(null);
        //dataElementDTO.setZcoord(null);
        return dataElementService.update(dataElementDTO);
    }

    @DeleteMapping("/")
    public String deleteByXY(@RequestParam Long pageId, @RequestParam Integer x, @RequestParam Integer y) {
        return dataElementService.deleteByXY(new DataElementDTO(pageId, x, y));
    }
}
