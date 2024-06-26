package com.jackbets.mybets.encryption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class PropertyServiceForJasyptStarter {

    @Value("${spring.datasource.password}")
    private String property;

    public String getProperty() {
        return property;
    }

    public String getPasswordUsingEnviro(Environment enviro) {
        return enviro.getProperty("spring.datasource.password");
    }
    
}
