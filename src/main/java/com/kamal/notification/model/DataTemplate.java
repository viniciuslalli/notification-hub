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
@NoArgsConstructor
public class DataTemplate {

    private String codigoProduto;
    private String evento;
    private String tituloPush;
    private String mensagemPush;
    private String urlDestino;
    private String plataformaCloud;
    private String idNotificacoes;

    public DataTemplate(PushNotification request) {
        this.codigoProduto = request.getCodigoProduto();
        this.evento = request.getEvento();
        this.tituloPush = request.getTituloPush();
        this.mensagemPush = request.getMensagemPush();
        this.urlDestino = request.getUrlDestino();
        this.plataformaCloud = request.getPlataformaCloud();
        this.idNotificacoes = request.getIdNotificacoes();
    }
}
