package com.hackerrank.api.repository;

import com.hackerrank.api.entity.GeoCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class GeoCodeRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public GeoCodeRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public GeoCode save(GeoCode geocode) {
        if (geocode == null) return null;
        String redisKey = geocode.getCountry() + "_" + geocode.getCity();
        // make country_city as key
        redisTemplate.opsForValue().set(redisKey, geocode);
        return geocode;
    }

    public GeoCode findByCountryAndCity(String country, String city) {
        String key = country + "_" + city;
        Object objectResponse = redisTemplate.opsForValue().get(key);
        if (objectResponse instanceof GeoCode) {
            return (GeoCode) objectResponse;
        }
        // Handle no result - to return an object with empty code
        GeoCode emptyResult = new GeoCode();
        emptyResult.setCountry(country);
        emptyResult.setCity(city);
        emptyResult.setCode(objectResponse == null ? "" : String.valueOf(objectResponse));
        return emptyResult;
    }

    public List<GeoCode> findAll() {
        Set<String> stringSet = redisTemplate.keys("*_*");
        List<GeoCode> geoCodeArrayList = new ArrayList<>();
        if (stringSet == null || stringSet.isEmpty()) return geoCodeArrayList;
        for (String key : stringSet) {
            Object val = redisTemplate.opsForValue().get(key);
            if (val instanceof GeoCode) {
                geoCodeArrayList.add((GeoCode) val);
            } else {
                // map key to country and city
                int identity = key.indexOf('_');
                String cntyObject = identity > 0 ? key.substring(0, identity) : key;
                String ctyObj = identity > 0 ? key.substring(identity + 1) : "";
                GeoCode newGeoCode = new GeoCode();
                newGeoCode.setCountry(cntyObject);
                newGeoCode.setCity(ctyObj);
                newGeoCode.setCode(val == null ? "" : String.valueOf(val));
                geoCodeArrayList.add(newGeoCode);
            }
        }
        return geoCodeArrayList;
    }
}