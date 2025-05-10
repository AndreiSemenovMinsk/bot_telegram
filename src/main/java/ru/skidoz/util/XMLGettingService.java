package ru.skidoz.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.stereotype.Service;

@Service
public class XMLGettingService {

    public InputStream getXMLRatesStream(String urlECBXMLSourceString) {
        try {
            URL url = new URL(urlECBXMLSourceString);

            System.out.println(urlECBXMLSourceString);

            return url.openStream();
        } catch (MalformedURLException e) {
//            logger.error("CurrencyService.scheduleRatesGetter MalformedURLException: " + e.getStackTrace());
        } catch (IOException e) {
//            logger.error("CurrencyService.scheduleRatesGetter IOException: " + e.getStackTrace());
        }
        return null;
    }

}
