package com.flab.rallymate.config;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

public class JasyptConfigTest extends JasyptConfig {

	@Disabled
	@ParameterizedTest
	@ValueSource(strings = {"1", "2"})
	public void 문자열을_jasypt로_암호화한다(String plainText) {
		StandardPBEStringEncryptor jasypt = new StandardPBEStringEncryptor();
		SimpleStringPBEConfig config = new SimpleStringPBEConfig();

		config.setPassword("encPassword");	// SET
		config.setPoolSize("1");
		config.setAlgorithm("algorithm");	// SET
		config.setProvider(new BouncyCastleProvider());
		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
		config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
		config.setStringOutputType("base64");
		config.setKeyObtentionIterations("1000");
		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
		jasypt.setConfig(config);

		String encryptedText = jasypt.encrypt(plainText);
		String decryptedText = jasypt.decrypt(encryptedText);

		assertThat(plainText).isEqualTo(decryptedText);
	}

}
