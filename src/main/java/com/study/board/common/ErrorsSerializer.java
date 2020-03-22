package com.study.board.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

import java.io.IOException;

/**
 * Controller 단에서 파라미터 유효성 검사 결과로 발생한 errors 객체를
 * JSON 으로 Serialize 하기 위한 Component.
 */
@JsonComponent
public class ErrorsSerializer extends JsonSerializer<Errors> {

  @Override
  public void serialize(Errors errors, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    gen.writeStartArray();
    errors.getFieldErrors().forEach(e -> {
      try {
        gen.writeStartObject();
        gen.writeStringField("field", e.getField());
        gen.writeStringField("objectName", e.getObjectName());
        gen.writeStringField("code", e.getCode());
        gen.writeStringField("defaultMessage", e.getDefaultMessage());
        Object rejectedValue = e.getRejectedValue();
        if (rejectedValue != null) {
          gen.writeStringField("rejectedValue", rejectedValue.toString());
        }
        gen.writeEndObject();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    });

    errors.getGlobalErrors().forEach(e -> {
      try {
        gen.writeStartObject();
        gen.writeStringField("objectName", e.getObjectName());
        gen.writeStringField("code", e.getCode());
        gen.writeStringField("defaultMessage", e.getDefaultMessage());
        gen.writeEndObject();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    });
    gen.writeEndArray();
  }
}