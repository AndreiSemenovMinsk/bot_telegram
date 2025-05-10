package ru.skidoz.mapper.side;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.skidoz.mapper.EntityMapper;
import ru.skidoz.model.entity.BasketProductEntity;
import ru.skidoz.model.pojo.side.BasketProduct;

@Component
@Mapper(componentModel = "spring", uses = {
        ProductMapper.class,
        BasketMapper.class})
public abstract class BasketProductMapper extends EntityMapper<BasketProduct, BasketProductEntity> {

    @Override
    @Mapping(source = "basket.id", target = "basket")
    @Mapping(source = "product.id", target = "product")
    public abstract BasketProduct toDto(BasketProductEntity entity);

    @Override
    @Mapping(source = "basket", target = "basket.id")
    @Mapping(source = "product", target = "product.id")
    public abstract BasketProductEntity toEntity(BasketProduct basketProduct);
}
