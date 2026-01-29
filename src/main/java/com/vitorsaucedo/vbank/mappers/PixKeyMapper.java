package com.vitorsaucedo.vbank.mappers;

import com.vitorsaucedo.vbank.dtos.PixKeyDetailsResponse;
import com.vitorsaucedo.vbank.dtos.PixKeyResponse;
import com.vitorsaucedo.vbank.entities.PixKey;
import com.vitorsaucedo.vbank.entities.User;
import org.springframework.stereotype.Component;

@Component
public class PixKeyMapper {

    public PixKeyResponse toResponse(PixKey key) {
        return new PixKeyResponse(
                key.getId(),
                key.getKeyType(),
                key.getKeyValue()
        );
    }

    public PixKeyDetailsResponse toDetailsResponse(PixKey pixKey) {
        User user = pixKey.getAccount().getUser();

        return new PixKeyDetailsResponse(
                maskName(user.getFullName()),
                maskDocument(user.getDocument()),
                "Vbank",
                pixKey.getAccount().getAccountNumber(),
                pixKey.getAccount().getAgency()
        );
    }

    private String maskName(String name) {
        return name.replaceAll("(?<=.{2}).(?=.* )|(?<=.{2}).(?=.{2}$)", "*");
    }

    private String maskDocument(String doc) {
        return doc.replaceAll("(\\d{3})\\d{5,8}(\\d{2})", "$1.***.***-$2");
    }
}
