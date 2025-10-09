package ru.skidoz.mapper.side;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import ru.skidoz.mapper.EntityMapper;
import ru.skidoz.mapper.telegram.BotMapper;
import ru.skidoz.mapper.telegram.UsersMapper;
import ru.skidoz.model.entity.ShopEntity;
import ru.skidoz.model.entity.telegram.UserEntity;
import ru.skidoz.model.pojo.side.Shop;

@Component
@Mapper(componentModel = "spring", uses = {
        UsersMapper.class,
        ShopGroupMapper.class,
        ProductMapper.class,
        BotMapper.class/*,
        ActionMapper.class*/})
public abstract class ShopMapper extends EntityMapper<Shop, ShopEntity> {

    @Override
    @Mapping(target = "productList", ignore = true)
    @Mapping(target = "actionList", ignore = true)
    @Mapping(target = "basketList", ignore = true)
    @Mapping(target = "cashbackList", ignore = true)
    @Mapping(target = "purchaseList", ignore = true)
    @Mapping(target = "cashbackWriteOffList", ignore = true)
    @Mapping(target = "debtorList", ignore = true)
    @Mapping(target = "creditorList", ignore = true)
    @Mapping(source = "sellerSet", target = "sellerSet", qualifiedByName = "idUserDtoList")
//    @Mapping(target = "currentConversationShopShopList", ignore = true)
    @Mapping(target = "purchaseShopGroupList", ignore = true)
//    @Mapping(source = "currentConversationShop.id", target = "currentConversationShop")
//    @Mapping(source = "currentConversationShopGroup.id", target = "currentConversationShopGroup")
//    @Mapping(source = "currentCreatingProduct.id", target = "currentCreatingProduct")
//    @Mapping(source = "currentCreatingAction.id", target = "currentCreatingAction")
    @Mapping(source = "adminUser.id", target = "adminUser")
    @Mapping(source = "bot.id", target = "bot")
    public abstract Shop toDto(ShopEntity entity);

    @Override
    @Mapping(target = "productList", ignore = true)
    @Mapping(target = "actionList", ignore = true)
    @Mapping(target = "basketList", ignore = true)
    @Mapping(target = "cashbackList", ignore = true)
    @Mapping(target = "purchaseList", ignore = true)
    @Mapping(target = "cashbackWriteOffList", ignore = true)
    @Mapping(source = "sellerSet", target = "sellerSet")
    @Mapping(target = "purchaseShopGroupList", ignore = true)
    @Mapping(source = "adminUser", target = "adminUser.id")//, qualifiedByName = "idUser")
    @Mapping(source = "bot", target = "bot.id")//, qualifiedByName = "idBot")
    public abstract ShopEntity toEntity(Shop shop);

    public ShopEntity toEntity(Integer shopId) {
        ShopEntity shop = new ShopEntity();
        shop.setId(shopId);
        return shop;
    }

    @Named("idUserDtoList")
    public List<Integer> idUserDtoList(List<UserEntity> ids) {
        List<Integer> sellerSet = new ArrayList<>();
        for (Iterator<UserEntity> it = ids.iterator(); it.hasNext(); ) {
            sellerSet.add(it.next().getId());
        }
        return  sellerSet;
    }

}
