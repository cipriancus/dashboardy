package com.ciprian.cusmuliuc.util.converter;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractConverter<T, K> implements Converter<T, K> {
  @Override
  public List<T> convertToEntityList(List<K> dtoList) {
    List<T> list = new ArrayList<>();
    for (K iterator : dtoList) {
      list.add(convertToEntity(iterator));
    }
    return list;
  }

  public abstract T convertToEntity(K dto);

  @Override
  public List<K> convertToDtoList(List<T> entityList) {
    List<K> list = new ArrayList<>();
    for (T iterator : entityList) {
      list.add(convertToDto(iterator));
    }
    return list;
  }

  public abstract K convertToDto(T entity);
}
