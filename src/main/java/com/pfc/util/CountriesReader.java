package com.pfc.util;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class CountriesReader {

    private static final String COUNTRIES = "countries.yaml";
    private final Yaml yaml;

    public CountriesReader() {
        yaml = new Yaml();
    }

    public Map<String, Integer> getIbanLengths() {
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(COUNTRIES);
        return yaml.load(inputStream);
    }
}
