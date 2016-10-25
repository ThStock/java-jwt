package com.auth0.jwtdecodejava.impl;

import com.auth0.jwtdecodejava.interfaces.Header;
import com.auth0.jwtdecodejava.interfaces.JWTPartsParser;
import com.auth0.jwtdecodejava.interfaces.Payload;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;

public class JWTParser implements JWTPartsParser {
    private ObjectMapper mapper;

    public JWTParser() {
        this(getDefaultObjectMapper());
    }

    public JWTParser(ObjectMapper mapper) {
        addDeserializers(mapper);
        this.mapper = mapper;
    }

    @Override
    public Payload parsePayload(String json) throws IOException {
        return mapper.readValue(json, Payload.class);
    }

    @Override
    public Header parseHeader(String json) throws IOException {
        return mapper.readValue(json, Header.class);
    }

    private void addDeserializers(ObjectMapper mapper) {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Payload.class, new PayloadDeserializer());
        module.addDeserializer(Header.class, new HeaderDeserializer());
        mapper.registerModule(module);
    }

    static ObjectMapper getDefaultObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        return mapper;
    }
}