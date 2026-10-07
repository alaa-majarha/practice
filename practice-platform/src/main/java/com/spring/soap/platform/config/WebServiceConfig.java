package com.spring.soap.platform.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@Configuration
@EnableWs
public class WebServiceConfig {

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet>
    messageDispatcherServlet(ApplicationContext context) {

        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(context);

        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }


    //////////  WSDL Config   /////////////

    // Schemas are injected by name: there is more than one XsdSchema bean, so injecting
    // by type would fail with NoUniqueBeanDefinitionException.

    @Bean(name = "account")
    public DefaultWsdl11Definition accountWsdl(
            @Qualifier("accountSchema") XsdSchema schema) {

        DefaultWsdl11Definition wsdl = new DefaultWsdl11Definition();

        wsdl.setPortTypeName("AccountPort");
        wsdl.setLocationUri("/ws");
        wsdl.setTargetNamespace("http://example.com/account");
        wsdl.setSchema(schema);

        return wsdl;
    }


    @Bean(name = "makePayment")
    public DefaultWsdl11Definition paymentWsdl(
            @Qualifier("paymentSchema") XsdSchema schema) {

        DefaultWsdl11Definition wsdl = new DefaultWsdl11Definition();

        wsdl.setPortTypeName("PaymentPort");
        wsdl.setLocationUri("/ws");
        wsdl.setTargetNamespace("http://bmct.com/makePayment");
        wsdl.setSchema(schema);

        return wsdl;
    }


    @Bean
    public XsdSchema accountSchema() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/account.xsd"));
    }


    @Bean
    public XsdSchema paymentSchema() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/makePayment.xsd"));
    }
}
