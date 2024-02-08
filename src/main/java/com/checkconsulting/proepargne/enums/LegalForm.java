package com.checkconsulting.proepargne.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum LegalForm {
    SASU,
    SAS,
    EURL,
    SARL,
    SCI,
    SNC;

    // @JsonCreator
    // public static LegalForm forValues(String value) {
    // if (value == null) {
    // return null;
    // }
    // for (LegalForm v : values()) {
    // if (value.equals(v.name())) {
    // return v;
    // }
    // }
    // return null;
    // }
}
