package ru.skidoz.mapper.telegram;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.skidoz.mapper.EntityMapper;
import ru.skidoz.mapper.side.ShopMapper;
import ru.skidoz.model.entity.telegram.UserEntity;
import ru.skidoz.model.pojo.telegram.User;

@Component
@Mapper(componentModel = "spring", uses = {
        ShopMapper.class,
        BotMapper.class,
        ButtonMapper.class,
        LevelMapper.class,
        MessageMapper.class})
public abstract class UsersMapper extends EntityMapper<User, UserEntity> {

    @Override
    @Mapping(target = "givenRecommendationList", ignore = true)
    @Mapping(target = "takenRecommendationList", ignore = true)
    @Mapping(target = "buyerBotList", ignore = true)
    @Mapping(target = "sellerShop", ignore = true)
    @Mapping(target = "shopList", ignore = true)
    @Mapping(target = "purchaseList", ignore = true)
    @Mapping(target = "basketList", ignore = true)
    @Mapping(target = "bookmarksList", ignore = true)
    @Mapping(target = "cashbackList", ignore = true)
    @Mapping(target = "cashbackWriteOffList", ignore = true)
    @Mapping(target = "scheduleBuyerList", ignore = true)
    @Mapping(target = "levels", ignore = true)
//    @Mapping(source = "currentLevelBeforeInterruption.id", target = "currentLevelBeforeInterruption")
//    @Mapping(source = "currentChangingBot.id", target = "currentChangingBot")
//    @Mapping(source = "currentChangingButton.id", target = "currentChangingButton")
//    @Mapping(source = "currentChangingMessage.id", target = "currentChangingMessage")
    public abstract User toDto(UserEntity entity);

    @Override
    @Mapping(target = "givenRecommendationList", ignore = true)
    @Mapping(target = "takenRecommendationList", ignore = true)
    @Mapping(target = "buyerBotList", ignore = true)
    @Mapping(target = "sellerShop", ignore = true)
    @Mapping(target = "shopList", ignore = true)
    @Mapping(target = "purchaseList", ignore = true)
    @Mapping(target = "basketList", ignore = true)
    @Mapping(target = "bookmarksList", ignore = true)
    @Mapping(target = "cashbackList", ignore = true)
    @Mapping(target = "cashbackWriteOffList", ignore = true)
    @Mapping(target = "scheduleBuyerList", ignore = true)
    @Mapping(target = "levels", ignore = true)
//    @Mapping(source = "currentLevelBeforeInterruption", target = "currentLevelBeforeInterruption.id")//, qualifiedByName = "idLevel")
//    @Mapping(source = "currentAdminShop", target = "currentAdminShop", qualifiedByName = "requestReplace")
//    @Mapping(source = "currentConstructShop", target = "currentConstructShop", qualifiedByName = "requestReplace")
//    @Mapping(source = "currentConversationShop", target = "currentConversationShop", qualifiedByName = "requestReplace")
//    @Mapping(source = "currentChangingBot", target = "currentChangingBot.id")//, qualifiedByName = "idBot")
//    @Mapping(source = "currentChangingButton", target = "currentChangingButton.id")//, qualifiedByName = "idButton")
//    @Mapping(source = "currentChangingMessage", target = "currentChangingMessage.id")//, qualifiedByName = "idMessage")
    public abstract UserEntity toEntity(User entity);


    /*@Named("idUserSet")
    public List<Users> idUser(List<Integer> ids) {
        List<Users> sellerSet = new ArrayList<>();
        for (Iterator<Integer> it = ids.iterator(); it.hasNext(); ) {
            sellerSet.add(idUser(it.next()));
        }
        return  sellerSet;
    }*/

    public UserEntity toEntity(Integer entityId) {
        UserEntity users = new UserEntity();
        users.setId(entityId);
        return users;
    }

    /*@Named("idUserDtoList")
    public List<Integer> idUserDtoList(List<Users> ids) {
        List<Integer> usersList = new ArrayList<>();
        for (Iterator<Users> it = ids.iterator(); it.hasNext(); ) {
            usersList.add(it.next().getId());
        }
        return  usersList;
    }*/
}
