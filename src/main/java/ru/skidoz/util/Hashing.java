package ru.skidoz.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author andrey.semenov
 */
public class Hashing {

    public static String sha256(String data) {
        try {
            // Создаём объект MessageDigest с алгоритмом SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Получаем хеш в виде массива байтов
            byte[] hashBytes = digest.digest(data.getBytes(StandardCharsets.UTF_8));

            // Конвертируем байты в шестнадцатеричную строку
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Ошибка: алгоритм SHA-256 не найден", e);
        }
    }
}
