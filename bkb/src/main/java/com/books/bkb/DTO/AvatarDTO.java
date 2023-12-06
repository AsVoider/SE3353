package com.books.bkb.DTO;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class AvatarDTO {
    public String username;
    public MultipartFile file;
}
