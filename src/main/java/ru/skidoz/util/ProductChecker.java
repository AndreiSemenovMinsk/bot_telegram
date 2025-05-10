package ru.skidoz.util;

import ru.skidoz.model.pojo.main.Action;
import ru.skidoz.model.pojo.side.Product;

/**
 * @author andrey.semenov
 */
public class ProductChecker {

    public static boolean checkProductSource(Action action, Product product) {

        System.out.println("@@@@@action.getProductSource()+++" + action.getProductSource());

        if (action.getProductSource() == null) {
            return true;
        }
        Integer actionProductSourceId = action.getProductSource();

        System.out.println("@@@actionProductSourceId+++" + actionProductSourceId);

        return actionProductSourceId.equals(product.getCategorySuperGroup())
                || actionProductSourceId.equals(product.getCategoryGroup())
                || actionProductSourceId.equals(product.getCategory())
                || actionProductSourceId.equals(product.getId());
    }

    public static boolean checkProductDTOTarget(Integer actionProductTarget, Product product) {
        if (actionProductTarget == null) {
            return true;
        }
        return actionProductTarget == null
                || product.getCategorySuperGroup().equals(actionProductTarget)
                || product.getCategoryGroup().equals(actionProductTarget)
                || product.getCategory().equals(actionProductTarget);
    }

}
