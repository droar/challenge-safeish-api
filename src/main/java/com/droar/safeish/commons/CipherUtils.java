/*
 * 
 */
package com.droar.safeish.commons;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import com.google.crypto.tink.subtle.AesGcmJce;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class CipherUtils.
 * 
 * This is a simple cipher util class, that provides argon2 hashing using a salt for custom hashing (encrypt decrypt).
 * It also provides BCrypt encoding and matching for password and security.
 * 
 * 
 * Encryption and Decryption is possible using a default password (as of now) this password can be
 * changed from the properties, or could also be provided from the API itself (future improvemet)
 * 
 * @author droar
 *
 */
@Component
@Slf4j
public class CipherUtils {

  /** The cipher password. */
  @Value("${cipher.password}")
  private String cipherPassword;

  /** The password hasher. */
  private Argon2CustomHasher customHasher;

  /** The password encoder. */
  private BCryptPasswordEncoder passwordEncoder;

  /**
   * Salt con la que se quiere generar las claves Argon2
   * 
   * @param salt
   */
  public CipherUtils(@Value("${cipher.salt}") String salt) {
    this.customHasher = new Argon2CustomHasher(salt);
    this.passwordEncoder = new BCryptPasswordEncoder();
  }
  
  /**
   * Encrypt password.
   *
   * @param passwordToEncode the password to encode
   * @return the string
   */
  public String encryptPassword(String passwordToEncode) {
    return this.passwordEncoder.encode(passwordToEncode);
  }
  
  /**
   * Password matches raw from encrypted one
   *
   * @param rawPassword the raw password
   * @param encryptedPassword the encrypted password
   * @return the boolean
   */
  public Boolean passwordMatchesEncrypted(String rawPassword, String encryptedPassword) {
    return this.passwordEncoder.matches(rawPassword, encryptedPassword);
  }

  /**
   * Encrypts provided string
   *
   * @param strToEncrypt the str to encrypt
   * @return the string
   * @throws GeneralSecurityException the general security exception
   */
  public String encryptString(String strToEncrypt) {
    String encryptedString = "";

    try {
      byte[] passWordHashed = this.customHasher.cipher(this.cipherPassword);
      AesGcmJce agjEncryption = new AesGcmJce(passWordHashed);
      byte[] encrypted = agjEncryption.encrypt(strToEncrypt.getBytes(), null);

      encryptedString = new String(Base64.getEncoder().encode(encrypted));
    } catch (GeneralSecurityException e) {
      log.warn("Provided string couldn't be encrypted");
    }

    return encryptedString;
  }

  /**
   * Decrypts provided string
   *
   * @param strToDecrypt the str to decrypt
   * @return the string
   * @throws GeneralSecurityException the general security exception
   */
  public String decryptString(String strToDecrypt) {
    String decryptedString = "";

    try {
      byte[] encrypted = Base64.getDecoder().decode(strToDecrypt.getBytes());
      byte[] passWordHashed = this.customHasher.cipher(this.cipherPassword);
      // Decryption
      AesGcmJce agjDecryption = new AesGcmJce(passWordHashed);
      byte[] decrypted = agjDecryption.decrypt(encrypted, null);
      return new String(decrypted, StandardCharsets.UTF_8);

    } catch (GeneralSecurityException e) {
      log.warn("Provided string couldn't be decrypted");
    }
    return decryptedString;
  }
}
