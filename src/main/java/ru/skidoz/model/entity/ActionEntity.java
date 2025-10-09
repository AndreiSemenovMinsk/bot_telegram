package ru.skidoz.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

//@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class ActionEntity extends AbstractEntity implements Serializable {

    @NotNull
    private String name;

    @NotNull
    @ManyToOne
    private ShopEntity shop;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ActionTypeEnum type;

    @NotNull
    private boolean towardFriend;

    @NotNull
    private boolean accommodateSum = true;

    private String description;

    @OneToMany(mappedBy="action")
    private List<CashbackEntity> cashbackList = new ArrayList<>();

//    @OneToMany(mappedBy="action")
//    private List<CashbackWriteOff> cashbackWriteOffList = new ArrayList<>();

//    @OneToMany(mappedBy="currentCreatingAction")
//    private List<ShopEntity> currentCreatingActionShopList = new ArrayList<>();
/*
    @OneToMany(mappedBy="action")
    private List<CashbackWriteOff> cashbackWriteOffList = new ArrayList<>();*/

//    @OneToMany(mappedBy="sourceAtAction")
//    private List<AbstractGroupEntity> productSourceList = new ArrayList<>();
//
//    @OneToMany(mappedBy="targetAtAction")
//    private List<AbstractGroupEntity> productTargetList = new ArrayList<>();

    @ManyToOne
    private AbstractGroupEntity productSource;

    @ManyToOne
    private AbstractGroupEntity productTarget;

    //Введите размер уровня суммы
//    @Getter(AccessLevel.NONE)
    private String levelSumString;
    //Введите в % размер начисляемого кэшбека
//    @Getter(AccessLevel.NONE)
    private String levelRatePreviousPurchaseList;

    //"Введите в % максимальную долю списания кэшбека в стоимости последуюшей покупке"
    private Integer futurePurchaseRate;
    //Введите в % размер кешбека для партнера
    private Integer friendFuturePurchaseRate;

    private Integer numberCoupon;

    private boolean active = false;

    public ActionEntity(Consumer<ActionEntity> builder){
        builder.accept(this);
    }

    public ActionEntity(Integer id){
        super.id = id;
    }

    public ActionEntity() {
        super();
    }

//    public void setLevelSumList(List<Integer> levelSumList) {
//        List<String> strings = new ArrayList<>(levelSumList.size());
//        for (Integer levelSum : levelSumList) {
//            strings.add(levelSum.toString());
//        }
//        levelSumString = String.join(":", strings);
//    }

//    public List<Integer> getLevelSumList() {
//
//        List<Integer> result = new ArrayList<>();
//        if (levelSumString != null) {
//            String[] arr = levelSumString.split(":");
//            for (String str : arr) {
//                result.add(Integer.valueOf(Structures.parseLong(str)));
//            }
//        }
//        return result;
//    }

    public List<Integer> accessLevelRatePreviousPurchaseList() {

        List<Integer> result = new ArrayList<>();
        String[] arr = levelRatePreviousPurchaseList.split(":");
        for (String str : arr) {
            result.add(Integer.valueOf(str));
        }
        return result;
    }

//    public void addProductSource(AbstractGroupEntity abstractGroupEntity){
//        productSourceList.add(abstractGroupEntity);
//    }
//
//    public void addProductTarget(AbstractGroupEntity abstractGroupEntity){
//        productTargetList.add(abstractGroupEntity);
//    }

    @Override
    public String toString() {
        return "Action{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", towardFriend=" + towardFriend +
                ", accommodateSum=" + accommodateSum +
                ", description='" + description + '\'' +
                ", levelSumString='" + levelSumString + '\'' +
                ", levelRatePreviousPurchaseList='" + levelRatePreviousPurchaseList + '\'' +
                ", rateFuturePurchase=" + futurePurchaseRate +
                ", rateFriendFuturePurchase=" + friendFuturePurchaseRate +
                ", numberCoupon=" + numberCoupon +
                ", active=" + active +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActionEntity)) return false;
        if (!super.equals(o)) return false;
        ActionEntity action = (ActionEntity) o;
        return super.id.equals(action.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), super.getId());
    }
}

