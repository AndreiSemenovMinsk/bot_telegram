package ru.skidoz.service.batch;

import static ru.skidoz.service.initializers.InitialLevel.SHOP;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.skidoz.service.initializers.AdminLevelInitializer;
import ru.skidoz.service.initializers.InitialLevel;
import ru.skidoz.service.initializers.InitializeEntries;
import ru.skidoz.service.initializers.ShopLevelInitializer;
import ru.skidoz.service.initializers.SkidozonaStartLevelInitializer;
import ru.skidoz.service.initializers.UserLevelInitializer;

/**
 * Created by Users on 30.05.2020.
 */
@Component
public class InitialLevelCashbackTasklet implements Tasklet {

    @Autowired
    InitialLevel initialLevel;
    @Autowired
    UserLevelInitializer userLevelInitializer;
    @Autowired
    AdminLevelInitializer adminLevelInitializer;
    @Autowired
    SkidozonaStartLevelInitializer skidozonaStartLevelInitializer;
    @Autowired
    ShopLevelInitializer shopLevelInitializer;
    @Autowired
    InitializeEntries initializeEntries;


    @Override
    public void execute() {

        System.out.println();
        System.out.println();
        System.out.println("execute----" + SHOP);
        System.out.println();
        System.out.println();

        if (SHOP == null) {

            System.out.println("@**@*@**@*@****@*@*@@@");

            initialLevel.initLevels();
            skidozonaStartLevelInitializer.initLevels();
            adminLevelInitializer.initLevels();
            shopLevelInitializer.initLevels();
            userLevelInitializer.initLevels();
            initializeEntries.initLevels();
        }

    }
}
