package ru.skidoz.service.batch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.skidoz.service.InitialLevel;
import ru.skidoz.service.LevelInitializer;

/**
 * Created by Users on 30.05.2020.
 */
@Component
public class InitialLevelCashbackTasklet implements Tasklet {

    @Autowired
    LevelInitializer levelInitializer;

    @Override
    public void execute() {

        if (InitialLevel.SHOP == null) {

            System.out.println("@**@*@**@*@****@*@*@@@");

            levelInitializer.initLevels();
        }

    }
}
