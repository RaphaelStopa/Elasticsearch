package com.example.elastic.controller;

import com.example.elastic.domain.Any;
import com.example.elastic.dto.SearchRequestDTO;
import com.example.elastic.service.AnyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/any")
public class AnyController {

    private final AnyService service;

    @Autowired
    public AnyController(AnyService service) {
        this.service = service;
    }

//    @Autowired
//    public VehicleController(VehicleService service, VehicleDummyDataService dummyDataService) {
//        this.service = service;
//        this.dummyDataService = dummyDataService;
//    }

    @PostMapping
    public void index(@RequestBody final Any any) {
        service.index(any);
    }

//    @PostMapping("/insertdummydata")
//    public void insertDummyData() {
//        dummyDataService.insertDummyData();
//    }

    @GetMapping("/{id}")
    public Any getById(@PathVariable final String id) {
        return service.getById(id);
    }

    @PostMapping("/search")
    public List<Any> search(@RequestBody final SearchRequestDTO dto) {
        return service.search(dto);
    }

    @GetMapping("/search/{date}")
    public List<Any> getAllVehiclesCreatedSince(
            @PathVariable
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            final Date date) {
        return service.getAllAnyCreatedSince(date);
    }

    @PostMapping("/searchcreatedsince/{date}")
    public List<Any> searchCreatedSince(
            @RequestBody final SearchRequestDTO dto,
            @PathVariable
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            final Date date) {
        return service.searchCreatedSince(dto, date);
    }
}
