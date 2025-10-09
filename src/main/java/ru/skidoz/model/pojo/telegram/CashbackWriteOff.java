package ru.skidoz.model.pojo.telegram;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ru.skidoz.model.pojo.AbstractDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CashbackWriteOff extends AbstractDTO implements Serializable {
//    @Autowired
//    private UserRepository usersRepository;
//    @Autowired
//    private ShopRepository shopRepository;
//    @Autowired
//    private ActionRepository actionRepository;
    private Integer sum;

    private Integer numberCoupon;

    @NotNull
    private boolean approved;

    @NotNull
    private Integer/*ShopDTO*/ shop;

    @NotNull
    private Integer/*User*/ user;

    private Integer/*ActionDTO*/ bestAction;

    public CashbackWriteOff(Consumer<CashbackWriteOff> builder){
        super();
        builder.accept(this);
    }

    public CashbackWriteOff() {
        super();
    }

//    public User getUsers() {
//        return usersRepository.findById(users);
//    }
//    public Action(egetAction() {
//        return actionRepository.findById(action);
//    }
//    public Shop(egetShop() {
//        return shopRepository.findById(shop);
//    }

    @Override
    public String toString() {
        return "CashbackWriteOffDTO{" +
                "id=" + getId() +
                ", sum=" + sum +
                ", numberCoupon=" + numberCoupon +
                ", approved=" + approved +
                ", shop=" + shop +
                ", users=" + user +
//                ", action=" + action +
                '}';
    }

}

