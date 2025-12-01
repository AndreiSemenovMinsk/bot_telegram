package ru.skidoz.mapper.side;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.skidoz.mapper.EntityMapper;
import ru.skidoz.mapper.category.CategoryGroupMapper;
import ru.skidoz.mapper.category.CategoryMapper;
import ru.skidoz.mapper.category.CategorySuperGroupMapper;
import ru.skidoz.model.entity.ProductEntity;
import ru.skidoz.model.pojo.side.Product;

@Component
@Mapper(componentModel = "spring", uses = {
        ShopMapper.class,
        CategoryMapper.class,
        CategoryGroupMapper.class,
        CategorySuperGroupMapper.class})
public abstract class ProductMapper extends EntityMapper<Product, ProductEntity> {

    @Override
    @Mapping(source = "shop.id", target = "shop")
    @Mapping(source = "category.id", target = "category")
    @Mapping(source = "categoryGroup.id", target = "categoryGroup")
    @Mapping(source = "categorySuperGroup.id", target = "categorySuperGroup")
    public abstract Product toDto(ProductEntity product);

    @Override
    @Mapping(source = "shop", target = "shop.id")
    @Mapping(source = "category", target = "category.id")//, qualifiedByName = "idCat")
    @Mapping(source = "categoryGroup", target = "categoryGroup.id")//, qualifiedByName = "idCatG")
    @Mapping(source = "categorySuperGroup", target = "categorySuperGroup.id")//, qualifiedByName = "idCatSG")
    @Mapping(target = "nameWordProductList", ignore = true)
    @Mapping(target = "scheduleDefaultList", ignore = true)
    @Mapping(target = "scheduleBuyerList", ignore = true)
    @Mapping(target = "basketProductList", ignore = true)
    @Mapping(target = "bookmarkList", ignore = true)
    @Mapping(target = "categoryFilterProductList", ignore = true)
//    @Mapping(target = "currentCreatingProductShopList", ignore = true)
    public abstract ProductEntity toEntity(Product product);


/*
    default ProductDTO toDto(CatSG categorySuperGroup) {
        return new Producte -> {
            e.setId(categorySuperGroup.getId());
            e.setName(categorySuperGroup.getAlias());
            e.setType("CatSG");
        });
    }

    default ProductDTO toDto(CatG categoryGroup) {
        return new Producte -> {
            e.setId(categoryGroup.getId());
            e.setName(categoryGroup.getAlias());
            e.setType("CatG");
        });
    }

    default ProductDTO toDto(Cat category) {
        return new Producte -> {
            e.setId(category.getId());
            e.setName(category.getAlias());
            e.setType("Cat");
        });
    }*/
}
