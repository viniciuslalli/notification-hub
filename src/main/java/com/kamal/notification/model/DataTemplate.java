package com.kamal.notification.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataTemplate {

    private String codigoProduto;
    private String evento;
    private String mensagemPush;
    private String urlDestino;
    private String plataformaCloud;
    private String idNotificacoes;





    public DataTemplate(PushNotification request) {
        this.codigoProduto = request.getCodigoProduto();
        this.evento = request.getEvento();
        this.mensagemPush = request.getMensagemPush();
        this.urlDestino = request.getCodigoCAR();
        this.idNotificacoes = request.getIdNotificacoes();
        this.plataformaCloud = "AZURE";


    }
}
