package com.kamal.notification.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@NoArgsConstructor
public class IosTemplate {

    private Alert aps;

    public IosTemplate(Alert alert) {
        this.aps = alert;
    }

}
