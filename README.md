# Spring ApplicationContext Builder

A utility to programmatically construct Spring ApplicationContexts using annotation or XML-based configurations.

## Usage

Construct a `SpringContextBuilder` instance, then add either XML or annotation-based configurations, and then call
`build()`.

```java
ApplicationContext context = new SpringContextBuilder()
    .addParentContextBean("dataSource", managedDataSource)
    .addParentContextBean("dropwizardConfiguration", configuration)
    .addAnnotationConfiguration(AcmeCoreSpringConfiguration.class)
    .addAnnotationConfiguration(AcmeDataSpringConfiguration.class)
    .addAnnotationConfiguration(AcmeSecuritySpringConfiguration.class)
    .build();
```

If you are adding multiple configurations, you can use the "with" form:

```java
ApplicationContext context = new SpringContextBuilder()
    .addParentContextBean("dataSource", managedDataSource)
    .addParentContextBean("dropwizardConfiguration", configuration)
    .withAnnotationConfigurations(AcmeCoreSpringConfiguration.class, 
                                  AcmeDataSpringConfiguration.class,
                                  AcmeSecuritySpringConfiguration.class)
    .build();
```

XML-based configurations are created in the same fashion:

```java
ApplicationContext context = new SpringContextBuilder()
    .addParentContextBean("dataSource", managedDataSource)
    .addParentContextBean("dropwizardConfiguration", configuration)
    .addXmlConfigLocation("applicationContext.xml")
    .build();
```

or using the "with" form to add multiple configurations:

```java
ApplicationContext context = new SpringContextBuilder()
    .addParentContextBean("dataSource", managedDataSource)
    .addParentContextBean("dropwizardConfiguration", configuration)
    .withXmlConfigLocations("appContext-core.xml", "appContext-data.xml", "appContext-security.xml")
    .build();
```

Finally, if you have _both_ XML and annotation-based configuration, you will need to import the XML configuration
in an annotation-based configuration class, for example:

```java
@Configuration
@ImportResource("applicationContext.xml")
public class XmlImportingConfiguration {
    
    // other configuration code...
}

ApplicationContext context = new SpringContextBuilder()
    .addParentContextBean("dataSource", managedDataSource)
    .addParentContextBean("dropwizardConfiguration", configuration)
    .addAnnotationConfiguration(XmlImportingConfiguration.class)
    .build();
```

Once you've constructed your application context, you can do whatever you need with it.
Most likely, you'll be getting some beans out of it...