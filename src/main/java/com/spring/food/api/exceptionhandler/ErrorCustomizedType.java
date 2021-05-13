package com.spring.food.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum ErrorCustomizedType {

    MENSAGEM_INCOMPREENSIVEL("/mensagem-incompreensivel", "Mensagem incompreensivel"),
    ENTIDADE_NAO_ENCONTRADA("/entidade-nao-encontrada", "Entidade não encontrada"),
    ENTIDADE_EM_USO("/entidade-em-uso", "Entidade em uso"),
    ERRO_NEGOCIO("/erro-negocio", "Violação de regra de negócio");

    private String title;
    private String uri;

    ErrorCustomizedType(String path, String title){
        this.uri = "https://spring-food" + path;
        this.title = title;
    }
}
