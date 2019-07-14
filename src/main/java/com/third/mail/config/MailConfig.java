package com.third.mail.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author bin.yin
 * @version 1.0
 * @createTime 2019/7/14 13:13
 * @since 1.0
 */

@Configuration
@PropertySource("classpath:config.properties")
public class MailConfig {
}
