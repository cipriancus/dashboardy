package com.ciprian.cusmuliuc.util.converter;

import java.util.List;

public interface Converter<T, K> {
  List<T> convertToEntityList(List<K> dtoList);

  T convertToEntity(K dto);

  List<K> convertToDtoList(List<T> entityList);

  K convertToDto(T entity);
}
