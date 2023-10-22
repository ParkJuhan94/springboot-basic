package org.prgrms.kdt;

import org.prgrms.kdt.app.configuration.YamlPropertiesFactory;
import org.prgrms.kdt.order.OrderProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ComponentScan(
    basePackages = {"org.prgrms.kdt.app", "org.prgrms.kdt.user", "org.prgrms.kdt.voucher"}
)
@PropertySource(value = "application.yaml", factory = YamlPropertiesFactory.class)
@EnableConfigurationProperties
public class KdtApplication {

  private static final Logger logger = LoggerFactory.getLogger(KdtApplication.class);

  public static void main(String[] args) {
    var applicationContext = SpringApplication.run(KdtApplication.class, args);
    var orderProperties = applicationContext.getBean(OrderProperties.class);
    logger.error("logger name => {}", logger.getName());
    logger.warn("version -> {}", orderProperties.getVersion());
    logger.warn("minimumOrderAmount -> {}", orderProperties.getMinimumOrderAmount());
    logger.warn("supportVendors -> {}", orderProperties.getSupportVendors());
    logger.warn("description -> {}", orderProperties.getDescription());

  }

}