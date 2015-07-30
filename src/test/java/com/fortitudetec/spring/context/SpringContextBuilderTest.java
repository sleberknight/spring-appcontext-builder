package com.fortitudetec.spring.context;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.StrictAssertions.catchThrowable;

public class SpringContextBuilderTest {

    private SpringContextBuilder _builder;

    @Before
    public void setUp() {
        _builder = new SpringContextBuilder();
    }

    private void assertMatch(SampleTestBean sampleTestBean, String expectedName, int expectedValue) {
        assertThat(sampleTestBean).isNotNull();
        assertThat(sampleTestBean.getName()).isEqualTo(expectedName);
        assertThat(sampleTestBean.getValue()).isEqualTo(expectedValue);
    }

    private void assertMatch(OtherTestBean otherTestBean,
                             String expectedName,
                             int expectedValue,
                             SampleTestBean expectedSampleTestBean) {

        assertThat(otherTestBean).isNotNull();
        assertThat(otherTestBean.getName()).isEqualTo(expectedName);
        assertThat(otherTestBean.getValue()).isEqualTo(expectedValue);
        assertThat(otherTestBean.getSampleTestBean()).isSameAs(expectedSampleTestBean);
    }

    @Test
    public void testRegisteringParentContextBeans() {
        SampleTestBean sampleTestBean1 = new SampleTestBean("test bean 1", 42);
        SampleTestBean sampleTestBean2 = new SampleTestBean("test bean 2", 84);

        ApplicationContext context = _builder
                .addParentContextBean("sampleTestBean1", sampleTestBean1)
                .addParentContextBean("sampleTestBean2", sampleTestBean2)
                .build();

        Map<String, SampleTestBean> sampleBeans = context.getParent().getBeansOfType(SampleTestBean.class);
        assertThat(sampleBeans).hasSize(2);

        assertThat(context.getBean("sampleTestBean1", SampleTestBean.class)).isSameAs(sampleTestBean1);
        assertThat(context.getBean("sampleTestBean2", SampleTestBean.class)).isSameAs(sampleTestBean2);
    }

    @Test
    public void testAddAnnotationConfiguration() {
        ApplicationContext context = _builder
                .addAnnotationConfiguration(SampleTestConfiguration.class)
                .build();

        SampleTestBean sampleTestBean1 = context.getBean("sampleTestBean1", SampleTestBean.class);
        assertMatch(sampleTestBean1, "test bean 1", 126);
        assertMatch(context.getBean("sampleTestBean2", SampleTestBean.class), "test bean 2", 197);
        assertMatch(context.getBean("otherTestBean", OtherTestBean.class), "other bean 1", 2048, sampleTestBean1);
    }

    @Test
    public void testAddAnnotationConfiguration_WhenXmlConfigLocationsHaveBeenSpecified() {
        Throwable thrown = catchThrowable(() ->
                _builder.addXmlConfigLocation("testApplicationContext.xml")
                        .addAnnotationConfiguration(SampleTestConfiguration.class)
                        .build());

        assertThat(thrown).isInstanceOf(IllegalStateException.class)
                .hasNoCause()
                .hasMessageContaining("XML config locations have already been specified");
    }

    @Test
    public void testWithAnnotationConfigurations() {
        ApplicationContext context = _builder
                .withAnnotationConfigurations(SampleTestConfiguration.class, SecondTestConfiguration.class)
                .build();

        assertMatch(context.getBean("sampleTestBean1", SampleTestBean.class), "test bean 1", 126);
        assertMatch(context.getBean("sampleTestBean2", SampleTestBean.class), "test bean 2", 197);
        assertMatch(context.getBean("sampleTestBean3", SampleTestBean.class), "test bean 3", 256);
    }

    @Test
    public void testWithAnnotationConfigurations_WhenXmlConfigLocationsHaveBeenSpecified() {
        Throwable thrown = catchThrowable(() ->
                _builder.addXmlConfigLocation("testApplicationContext.xml")
                        .withAnnotationConfigurations(SampleTestConfiguration.class)
                        .build());

        assertThat(thrown).isInstanceOf(IllegalStateException.class)
                .hasNoCause()
                .hasMessageContaining("XML config locations have already been specified");
    }

    @Test
    public void testAddXmlConfigLocation() {
        ApplicationContext context = _builder
                .addXmlConfigLocation("testApplicationContext.xml")
                .build();

        SampleTestBean sampleTestBean1 = context.getBean("sampleTestBean1", SampleTestBean.class);
        assertMatch(sampleTestBean1, "test bean 1", 42);
        assertMatch(context.getBean("sampleTestBean2", SampleTestBean.class), "test bean 2", 84);
        assertMatch(context.getBean("otherTestBean", OtherTestBean.class), "other bean 1", 4096, sampleTestBean1);
    }

    @Test
    public void testAddXmlConfigLocation_WhenAnnotationConfigurationsHasBeenSpecified() {
        Throwable thrown = catchThrowable(() ->
                _builder.addAnnotationConfiguration(SampleTestConfiguration.class)
                        .addXmlConfigLocation("testApplicationContext.xml")
                        .build());

        assertThat(thrown).isInstanceOf(IllegalStateException.class)
                .hasNoCause()
                .hasMessageContaining("Annotated classes have already been specified");
    }

    @Test
    public void testWithXmlConfigLocations() {
        ApplicationContext context = _builder
                .withXmlConfigLocations("testApplicationContext.xml", "secondTestApplicationContext.xml")
                .build();

        assertMatch(context.getBean("sampleTestBean1", SampleTestBean.class), "test bean 1", 42);
        assertMatch(context.getBean("sampleTestBean2", SampleTestBean.class), "test bean 2", 84);
        assertMatch(context.getBean("sampleTestBean3", SampleTestBean.class), "test bean 3", 256);
    }

    @Test
    public void testWithXmlConfigLocations_WhenAnnotationConfigurationHasBeenSpecified() {
        Throwable thrown = catchThrowable(() ->
                _builder.addAnnotationConfiguration(SampleTestConfiguration.class)
                        .withXmlConfigLocations("testApplicationContext.xml")
                        .build());

        assertThat(thrown).isInstanceOf(IllegalStateException.class)
                .hasNoCause()
                .hasMessageContaining("Annotated classes have already been specified");
    }

    @Test
    public void testUsingAnnotatedClasses_CombinedWith_ImportedXmlConfiguration() {
        ApplicationContext context = _builder
                .addAnnotationConfiguration(XmlImportingTestConfiguration.class)
                .addAnnotationConfiguration(SecondTestConfiguration.class)
                .build();

        assertMatch(context.getBean("sampleTestBean1", SampleTestBean.class), "test bean 1", 42);
        assertMatch(context.getBean("sampleTestBean2", SampleTestBean.class), "test bean 2", 84);
        assertMatch(context.getBean("sampleTestBean3", SampleTestBean.class), "test bean 3", 256);
    }
}
