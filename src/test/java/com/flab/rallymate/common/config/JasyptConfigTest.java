package com.flab.rallymate.common.config;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

public class JasyptConfigTest extends JasyptConfig {

    @Disabled
    @ParameterizedTest
    @ValueSource(strings = {"a", "b"})
    public void 문자열을_jasypt로_암호화한다(String plainText) {
        StandardPBEStringEncryptor jasypt = new StandardPBEStringEncryptor();
        jasypt.setPassword("password");

        String encryptedText = jasypt.encrypt(plainText);
        String decryptedText = jasypt.decrypt(encryptedText);

        System.out.println(decryptedText + " ===> " + encryptedText);

        assertThat(plainText).isEqualTo(decryptedText);
    }

}