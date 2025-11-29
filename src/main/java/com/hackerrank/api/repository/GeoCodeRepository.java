package com.hackerrank.api.repository;

import com.hackerrank.api.entity.GeoCode;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GeoCodeRepository {
    public GeoCode save(GeoCode geocode) {
        //TODO
        return null;
    }

    public GeoCode findByCountryAndCity(String country, String city) {
        //TODO
        return null;
    }

    public List<GeoCode> findAll() {
        //TODO
        return null;
    }
}