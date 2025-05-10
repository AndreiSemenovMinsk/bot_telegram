package ru.skidoz.mapper.side;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.skidoz.mapper.EntityMapper;
import ru.skidoz.mapper.telegram.UsersMapper;
import ru.skidoz.model.entity.BookmarkEntity;
import ru.skidoz.model.pojo.side.Bookmark;

@Component
@Mapper(componentModel = "spring", uses = {ShopMapper.class, ProductMapper.class, UsersMapper.class})
public abstract class BookmarkMapper extends EntityMapper<Bookmark, BookmarkEntity> {

    @Override
    @Mapping(source = "user.id", target = "user")
    @Mapping(source = "product.id", target = "product")
    @Mapping(source = "shop.id", target = "shop")
    public abstract Bookmark toDto(BookmarkEntity entity);

    @Override
//    @Mapping(source = "shop", target = "shop", qualifiedByName = "idShop")
    @Mapping(source = "product", target = "product.id")//, qualifiedByName = "idPrd")
//    @Mapping(source = "users", target = "users", qualifiedByName = "idUser")
    public abstract BookmarkEntity toEntity(Bookmark bookmark);
}
