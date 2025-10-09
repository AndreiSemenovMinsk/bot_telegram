package ru.skidoz.mapper.side;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import ru.skidoz.mapper.EntityMapper;
import ru.skidoz.model.entity.ShopEntity;
import ru.skidoz.model.entity.ShopGroupEntity;
import ru.skidoz.model.entity.telegram.UserEntity;
import ru.skidoz.model.pojo.telegram.ShopGroup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
@Mapper(componentModel = "spring", uses = { ShopMapper.class})
public abstract class ShopGroupMapper extends EntityMapper<ShopGroup, ShopGroupEntity> {

    @Override
    @Mapping(source = "shopSet", target = "shopSet", qualifiedByName = "shopList")
    public abstract ShopGroup toDto(ShopGroupEntity entity);

    @Override
    public abstract ShopGroupEntity toEntity(ShopGroup shop);


    @Named("shopList")
    public List<Integer> shopList(List<ShopEntity> ids) {
        List<Integer> sellerSet = new ArrayList<>();
        for (Iterator<ShopEntity> it = ids.iterator(); it.hasNext(); ) {
            sellerSet.add(it.next().getId());
        }
        return  sellerSet;
    }

}
