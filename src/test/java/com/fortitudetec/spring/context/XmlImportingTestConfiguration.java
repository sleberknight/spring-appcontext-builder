package com.fortitudetec.spring.context;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("testApplicationContext.xml")
public class XmlImportingTestConfiguration {
}
