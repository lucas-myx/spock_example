package com.javakk.spock.mapper;

import com.javakk.spock.model.OrderDTO;
import com.javakk.spock.model.OrderVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {

    static final OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mappings({})
    OrderVO convert(OrderDTO requestDTO);

}
