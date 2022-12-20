package dev.henriqueluiz.travelling.security;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rsa")
public record RSAProperties(RSAPrivateKey privateKey, RSAPublicKey publicKey) {}
