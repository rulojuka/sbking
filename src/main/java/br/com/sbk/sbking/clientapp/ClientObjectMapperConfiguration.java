package br.com.sbk.sbking.clientapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import br.com.sbk.sbking.core.rulesets.abstractrulesets.Ruleset;
import br.com.sbk.sbking.networking.jackson.RulesetDeserializer;

// This configuration is not working
// Currently using directly in place at:
// RestHTTPClient constructor
@Configuration
public class ClientObjectMapperConfiguration {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapperOnlyFields = new ObjectMapper();

        // Configure to use fields in all classes
        objectMapperOnlyFields.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
        objectMapperOnlyFields.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);

        // Add custom Serializers
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Ruleset.class, new RulesetDeserializer());

        objectMapperOnlyFields.registerModule(module);

        return objectMapperOnlyFields;
    }
}
