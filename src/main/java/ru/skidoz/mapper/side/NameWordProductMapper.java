package ru.skidoz.mapper.side;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import ru.skidoz.mapper.EntityMapper;
import ru.skidoz.model.entity.NameWordEntity;
import ru.skidoz.model.entity.NameWordProductEntity;
import ru.skidoz.model.pojo.side.NameWordProduct;

@Component
@Mapper(componentModel = "spring", uses = {
        //NameWordRepository.class,
        ProductMapper.class})
public abstract class NameWordProductMapper extends EntityMapper<NameWordProduct, NameWordProductEntity> {

    @Override
    @Mapping(source = "product.id", target = "product")
    @Mapping(source = "nameWord.id", target = "nameWord")
    public abstract NameWordProduct toDto(NameWordProductEntity nameWordProduct);

    @Override
    @Mapping(source = "product", target = "product.id")//, qualifiedByName = "idPrd")
    @Mapping(source = "nameWord", target = "nameWord", qualifiedByName = "idNameWord")
    public abstract NameWordProductEntity toEntity(NameWordProduct nameWordProduct);

    @Named("idNameWord")
    public NameWordEntity idNameWord(Integer id) {
        return null;
        //return nameWordRepository.getOne(id);
    }
}
