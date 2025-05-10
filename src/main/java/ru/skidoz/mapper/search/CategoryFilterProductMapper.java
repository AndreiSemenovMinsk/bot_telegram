package ru.skidoz.mapper.search;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import ru.skidoz.mapper.EntityMapper;
import ru.skidoz.mapper.side.ProductMapper;
import ru.skidoz.model.entity.CategoryFilterProductEntity;
import ru.skidoz.model.pojo.search.menu.CategoryFilterProduct;

@Component
@Mapper(componentModel = "spring", uses = {FilterPointMapper.class, ProductMapper.class})
public abstract class CategoryFilterProductMapper extends EntityMapper<CategoryFilterProduct, CategoryFilterProductEntity> {

    @Mapping(source = "categoryFilterProduct", target = "hashCode", qualifiedByName = "getHash")
    @Mapping(source = "product.id", target = "product")
    @Override
    public abstract CategoryFilterProduct toDto(CategoryFilterProductEntity categoryFilterProduct);

    @Mapping(target = "product", ignore = true)
    @Override
    public abstract CategoryFilterProductEntity toEntity(CategoryFilterProduct categoryFilterProduct);

    @Named("getHash")
    public Integer getLevelRatePreviousPurchase(CategoryFilterProductEntity categoryFilterProduct) {

        return categoryFilterProduct.getValue() * 2_000_000 + categoryFilterProduct.getId().intValue();
    }
}
