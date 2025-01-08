package com.payvang.Invoice.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.core.env.Environment;


@Component
public class ConfigurationManager {

    private static Environment env;

    @Autowired
    public ConfigurationManager(Environment env) {
        ConfigurationManager.env = env;
    }

    public static String getProperty(String propertyName) {
        return env.getProperty(propertyName, "");
    }
}