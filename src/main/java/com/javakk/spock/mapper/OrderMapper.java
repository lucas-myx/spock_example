package com.javakk.spock.mapper;

import com.javakk.spock.model.OrderDTO;
import com.javakk.spock.model.OrderVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * 订单属性转换
 */
@Mapper
public interface OrderMapper {

    // 即使不用static final修饰，接口里的变量默认也是静态、final的
    static final OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mappings({})
    OrderVO convert(OrderDTO requestDTO);
}
