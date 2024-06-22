package com.softalchemy.accounts.dto;

import java.util.HashMap;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "accounts")
public record AccountsContactInfoDto(String message, HashMap<String,String> contactDetails,
                                     List<String>onCallSupport) {

}
