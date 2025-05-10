package ru.skidoz.model.pojo.main;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ru.skidoz.model.entity.ActionTypeEnum;
import ru.skidoz.model.pojo.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

import static ru.skidoz.util.Structures.sortTwoArrays;

@Setter
@Getter
public class Action extends AbstractDTO {

    private String name;
    private boolean accommodateSum = true;
    private String description;
    private Integer/*ShopDTO*/ shop;
    private boolean towardFriend;
    private boolean active = false;
    private ActionTypeEnum type;
    private Integer numberCoupon;
    private Integer rateFuturePurchase;
    private Integer rateFriendFuturePurchase;
    private Integer/*AbstractGroupDTO*/ productSource;
//    private List<Cashback> cashbackList = new ArrayList<>();
//    private List<AbstractGroup> productTargetList = new ArrayList<>();
    private Integer/*AbstractGroupDTO*/ productTarget;

    private BigDecimal accucmulatedSum = BigDecimal.ZERO;

    private List<BigDecimal> levelSumList = new ArrayList<>();

    private List<Integer> levelRatePreviousPurchaseList = new ArrayList<>();

    public Action(Consumer<Action> builder){
        super();
        builder.accept(this);
    }

    public Action() {
        super();
    }

    public List<BigDecimal> accessLevelSumList(){
        return levelSumList;
    }

    public void addRatePreviousPurchase(Integer val) {
        //numberCoupon = 123;
        if (levelRatePreviousPurchaseList.size() < 15) {

            levelRatePreviousPurchaseList.add(val);

            sortTwoArrays(levelSumList, levelRatePreviousPurchaseList);
        } else  {
            System.out.println("Too many levels");
        }
    }

    public void addLevelSum(Integer sum){
        if (levelSumList.size() < 15) {
            System.out.println("addLevelSum+" + sum);
            levelSumList.add(new BigDecimal(sum));
        } else  {
            System.out.println("Too many levels");
        }
    }

    public List<Integer> accessLevelRatePreviousPurchaseList() {
        return new ArrayList<>(levelRatePreviousPurchaseList);
    }

    @Override
    public String toString() {
        return "ActionDTO{" +
                "id=" + super.getId() +
                ", shop=" + shop +
                ", name=" + name +
                ", active=" + active +
                ", productSource=" + productSource +
                ", productTarget=" + productTarget +
                ", accommodateSum=" + accommodateSum +
                ", type=" + type +
                ", numberCoupon=" + numberCoupon +
                ", rateFuturePurchase=" + rateFuturePurchase +
                ", levelRatePreviousPurchaseList=" + levelRatePreviousPurchaseList +

                ", levelSumList=" + levelSumList  +
                '}';
    }
}
