package ru.skidoz.service.batch;

import ru.skidoz.service.InitialExcelMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Users on 30.05.2020.
 */
@Component
public class ExcelMenuTasklet implements Tasklet {

    @Autowired
    InitialExcelMenu initialExcelMenu;


    @Override
    public void execute() {

        initialExcelMenu.execute();
    }
}
