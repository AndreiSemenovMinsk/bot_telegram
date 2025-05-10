package ru.skidoz.model.pojo.telegram;

import java.io.Serializable;
import java.util.function.Consumer;

import ru.skidoz.model.pojo.AbstractDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Recommendation extends AbstractDTO implements Serializable {

//    @Autowired
//    private UserRepository usersRepository;
//    @Autowired
//    private ShopRepository shopRepository;

    // если дал рекомендацию магазин
    private Integer/*ShopDTO*/ shop;

    // тот, кто получил рекомендацию
    @NotNull
    private Integer/*User*/ buyer;

    // тот, кто дал рекомендацию
    private Integer/*User*/ friend;

    public Recommendation(Consumer<Recommendation> builder){
        super();
        builder.accept(this);
    }

    public Recommendation() {
        super();
    }

//    public Shop getShop() {
//        return shopRepository.findById(shop);
//    }
//
//    public User getBuyer() {
//        return usersRepository.findById(buyer);
//    }
//
//    public User getFriend() {
//        return usersRepository.findById(friend);
//    }

}

