package com.example.demo.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "profile_images")
public class ProfileImage extends Image {

    public ProfileImage(String imageUrl) {
        super(imageUrl);
    }

}
