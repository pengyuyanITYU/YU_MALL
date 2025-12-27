package com.yu.common.enums;

public enum Header {

    Authorization("Jwt");

    String spec;
    Header(String spec) {
        this.spec = spec;
    }

    @Override
    public String toString() {
        return Header.Authorization.toString();
    }
}
