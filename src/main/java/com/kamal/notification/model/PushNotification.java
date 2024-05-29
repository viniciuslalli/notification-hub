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
public class PushNotification {

    private String codigoProduto;
    private String evento;
    private String mensagemPush;
    private String urlDestino;
    private String codigoCAR;
    private String hashReceptor;

    private String tituloPush;
    private String plataformaCloud;
    private String idNotificacoes;


    private List<Device> destinatarios;

}
