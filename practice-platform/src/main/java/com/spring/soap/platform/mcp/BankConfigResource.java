package com.spring.soap.platform.mcp;

import org.springaicommunity.mcp.annotation.McpResource;
import org.springframework.stereotype.Service;

@Service
public class BankConfigResource {

    @McpResource(
            uri = "bank://configuration",
            name = "bank-configuration",
            description = "Static bank configuration (currency, country, limits)",
            mimeType = "text/plain"
    )
    public String getBankConfiguration() {
        return """
        Currency: JOD
        Country: Jordan
        Max Transaction: 50000
        """;
    }
}
