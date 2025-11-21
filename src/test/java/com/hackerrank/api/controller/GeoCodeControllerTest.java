package com.hackerrank.api.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackerrank.api.entity.GeoCode;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
class GeoCodeControllerTest {
    ObjectMapper om = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;

    List<GeoCode> getData() {
        GeoCode expected0 = GeoCode.builder()
                .country("United States")
                .city("Boston")
                .code("444556")
                .build();

        GeoCode expected1 = GeoCode.builder()
                .country("India")
                .city("Delhi")
                .code("444557")
                .build();

        GeoCode expected2 = GeoCode.builder()
                .country("Japan")
                .city("Tokyo")
                .code("744556")
                .build();

        GeoCode expected3 = GeoCode.builder()
                .country("Japan")
                .city("Ukase")
                .code("")
                .build();

        GeoCode expected4 = GeoCode.builder()
                .country("United States")
                .city("NYC")
                .code("")
                .build();
        List<GeoCode> list = new ArrayList<>();
        list.add(expected0);
        list.add(expected1);
        list.add(expected2);
        list.add(expected3);
        list.add(expected4);

        return list;
    }

    @Test
    public void addGeoCodeTest() throws Exception {
        for (GeoCode expected : getData()) {
            GeoCode actual = om.readValue(mockMvc.perform(post("/redis/geocode")
                            .contentType("application/json")
                            .content(om.writeValueAsString(expected)))
                    .andDo(print())
                    .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), GeoCode.class);

            Assert.assertEquals(expected.getCountry(), actual.getCountry());
            Assert.assertEquals(expected.getCity(), actual.getCity());
            Assert.assertEquals(expected.getCode(), actual.getCode());
        }
    }

    @Test
    public void findGeoCodeNonEmptyTest() throws Exception {
        GeoCode expected = GeoCode.builder()
                .country("United States")
                .city("Boston")
                .build();

        GeoCode actual = om.readValue(mockMvc.perform(get("/redis/geocode/" + expected.getCountry() + "/" + expected.getCity())
                        .contentType("application/json")
                        .content(om.writeValueAsString(expected)))
                .andDo(print())
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), GeoCode.class);

        Assert.assertEquals(expected.getCountry(), actual.getCountry());
        Assert.assertEquals(expected.getCity(), actual.getCity());
        Assert.assertEquals("444556", actual.getCode());
    }

    @Test
    public void findGeoCodeEmptyTest() throws Exception {
        GeoCode expected = GeoCode.builder()
                .country("United States")
                .city("NYC")
                .build();

        GeoCode actual = om.readValue(mockMvc.perform(get("/redis/geocode/" + expected.getCountry() + "/" + expected.getCity())
                        .contentType("application/json")
                        .content(om.writeValueAsString(expected)))
                .andDo(print())
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), GeoCode.class);

        Assert.assertEquals(expected.getCountry(), actual.getCountry());
        Assert.assertEquals(expected.getCity(), actual.getCity());
        Assert.assertEquals("", actual.getCode());
    }


    @Test
    public void findGeoCodeAllTest() throws Exception {
        Map<Object, Object> expected = getData().stream().collect(Collectors.toMap(g -> g.getCountry() + "_" + g.getCity(), g -> g.getCode()));

        List<GeoCode> actuals = om.readValue(mockMvc.perform(get("/redis/geocode")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), new TypeReference<List<GeoCode>>() {
        });


        for (GeoCode actual : actuals) {
            String key = actual.getCountry() + "_" + actual.getCity();
            Assert.assertEquals(expected.get(key), actual.getCode());
        }
    }
}
