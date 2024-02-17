package com.example.TravelPlanner.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class MapperUtil {

    private static final ModelMapper modelMapper = new ModelMapper();
    private static final ObjectMapper objectMapper = new ObjectMapper();


    public static <S, T> T map(S source, Class<T> targetClass) {
        return modelMapper.map(source, targetClass);
    }

    public static String convertPojoToJson(Object pojo) {
        try {
            return objectMapper.writeValueAsString(pojo);
        } catch (Exception e) {
            throw new RuntimeException("Error converting POJO to JSON", e);
        }
    }

    public static <T> T convertJsonToPojo(String json, Class<T> pojoClass) {
        try {
            return objectMapper.readValue(json, pojoClass);
        } catch (Exception e) {
            throw new RuntimeException("Error converting JSON to POJO", e);
        }
    }

    public static <S, T> List<T> mapList(List<S> sourceList, Class<T> targetClass){
        List<T> targetList = new ArrayList<>();
        for(S sourceItem : sourceList){
            T mapItem = map(sourceItem, targetClass);
            targetList.add(mapItem);
        }
        return targetList;
    }
}
