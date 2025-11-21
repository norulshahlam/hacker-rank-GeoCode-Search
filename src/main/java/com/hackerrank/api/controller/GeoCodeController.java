package com.hackerrank.api.controller;

import com.hackerrank.api.entity.GeoCode;
import com.hackerrank.api.repository.GeoCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/redis")
public class GeoCodeController {
    private final GeoCodeRepository geocodeRepository;

    @Autowired
    public GeoCodeController(GeoCodeRepository geocodeRepository) {
        this.geocodeRepository = geocodeRepository;
    }

    //create geocode
    @PostMapping("/geocode")
    public ResponseEntity<GeoCode> createGeocode(@RequestBody GeoCode geocode) {
        GeoCode _tutorial = geocodeRepository.save(geocode);
        return new ResponseEntity<>(_tutorial, HttpStatus.CREATED);
    }

    @GetMapping("/geocode")
    public ResponseEntity<List<GeoCode>> getGeocodes() {
        List<GeoCode> list = geocodeRepository.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/geocode/{country}/{city}")
    public ResponseEntity<GeoCode> getGeocode(@PathVariable String country, @PathVariable String city) {
        GeoCode geoCode = geocodeRepository.findByCountryAndCity(country, city);
        return new ResponseEntity<>(geoCode, HttpStatus.OK);
    }
}