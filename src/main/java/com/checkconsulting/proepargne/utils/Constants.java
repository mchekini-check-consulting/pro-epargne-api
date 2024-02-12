package com.checkconsulting.proepargne.utils;

import com.google.common.collect.Lists;

import java.util.List;

public class Constants {
    public static final List<String> GLOBAL_RESPONSE_IGNORE_PATHS = Lists.newArrayList("/v3/api-docs", "/v3/api-docs/swagger-config");
    public static final String ASSET_FILE_PATH = "classpath:files/historique-assets.csv";
}
