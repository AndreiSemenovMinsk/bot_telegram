package ru.skidoz.model.pojo.telegram;

import java.io.InputStream;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author andrey.semenov
 */
@NoArgsConstructor
public class Image {
    @Getter
    @Setter
    private InputStream image;
    @Getter
    @Setter
    private String description;

    public Image(InputStream image, String description) {
        this.image = image;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Image{" +
                "images=" + image +
                ", description='" + description + '\'' +
                '}';
    }
}
