package com.spring.food.api.exceptionhandler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.spring.food.domain.exception.EntidadeEmUsoException;
import com.spring.food.domain.exception.EntidadeNaoEncontradaException;
import com.spring.food.domain.exception.NegocioException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {



    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        if (rootCause instanceof InvalidFormatException) {
            return handleInvalidFormatException((InvalidFormatException) rootCause, headers, status, request);
        }
        String detail = "O corpo da requisição está inválido. Verifique erro de sintaxe.";
        ErrorCustomized problem = createErrorCustomizedBuilder(status, ErrorCustomizedType.MENSAGEM_INCOMPREENSIVEL, detail).build();
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex,
                                                                HttpHeaders headers, HttpStatus status, WebRequest request) {
        String path = ex.getPath().stream()
                .map(ref -> ref.getFieldName())
                .collect(Collectors.joining("."));
        String detail = String.format("A propriedade '%s' recebeu o valor '%s', "
                        + "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
                path, ex.getValue(), ex.getTargetType().getSimpleName());
        ErrorCustomized problem = createErrorCustomizedBuilder(status, ErrorCustomizedType.MENSAGEM_INCOMPREENSIVEL, detail).build();
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<?> handleEntidadeNaoEncontradaException(EntidadeNaoEncontradaException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorCustomized errorCustomized = createErrorCustomizedBuilder(status, ErrorCustomizedType.ENTIDADE_NAO_ENCONTRADA, ex.getMessage()).build();
        return handleExceptionInternal(ex, errorCustomized, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(EntidadeEmUsoException.class)
    public ResponseEntity<?> handleEntidadeEmUsoException(EntidadeEmUsoException ex, WebRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        ErrorCustomized errorCustomized = createErrorCustomizedBuilder(status, ErrorCustomizedType.ENTIDADE_EM_USO, ex.getMessage()).build();
        return handleExceptionInternal(ex, errorCustomized, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<?> handleNegocioException(NegocioException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorCustomized errorCustomized = createErrorCustomizedBuilder(status, ErrorCustomizedType.ERRO_NEGOCIO, ex.getMessage()).build();
        return handleExceptionInternal(ex, errorCustomized, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

        if (body == null) {
            body = ErrorCustomized.builder()
                    .title(status.getReasonPhrase())
                    .status(status.value())
                    .build();
        } else if (body instanceof String) {
            body = ErrorCustomized.builder()
                    .title((String) body)
                    .build();
        }

        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    private ErrorCustomized.ErrorCustomizedBuilder createErrorCustomizedBuilder(HttpStatus status,
                                                                                ErrorCustomizedType errorCustomizedType,
                                                                                String detail){
        return ErrorCustomized.builder()
                .status(status.value())
                .type(errorCustomizedType.getUri())
                .title(errorCustomizedType.getTitle())
                .detail(detail);
    }
}
