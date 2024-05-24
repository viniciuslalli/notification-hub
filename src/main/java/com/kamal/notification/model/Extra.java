package com.kamal.notification.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@AllArgsConstructor

public class Extra {
    private String plataformaCloud;
    private String idNotificacoes;

    public Extra(String idNotificacoes) {
        this.idNotificacoes = idNotificacoes;
        this.plataformaCloud = "AZURE";
    }
}
