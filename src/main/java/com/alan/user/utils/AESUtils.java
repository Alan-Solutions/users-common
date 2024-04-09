package com.alan.user.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

@Component
public class AESUtils {

  private static final Logger logger = LoggerFactory.getLogger(AESUtils.class.getName());
  private final String CIPHER_INSTANCE = "AES/ECB/PKCS5Padding";

  public String encrypt(final char[] password, String dynaKey) {
    try {
      Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE);
      SecretKeySpec key = createKey(dynaKey);
      cipher.init(Cipher.ENCRYPT_MODE, key);
      return Base64.getEncoder().encodeToString(cipher.doFinal(
              new String(password).getBytes(StandardCharsets.UTF_8)
      ));
    } catch (NoSuchPaddingException | NoSuchAlgorithmException
             | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
      logger.error("Exception occured while encrypt ", e);
    }
    return null;
  }

  public String decrypt(final char[] password, String dynaKey) {
    try {
      Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE);
      cipher.init(Cipher.DECRYPT_MODE, createKey(dynaKey));
      return new String(cipher.doFinal(Base64.getDecoder().decode(new String(password))));
    } catch (NoSuchPaddingException | NoSuchAlgorithmException
             | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
      logger.error("Exception occured while decrypt ", e);
    }
    return null;
  }

  private SecretKeySpec createKey(final String dynaKey) {
    MessageDigest sha = null;
    byte[] key;
    SecretKeySpec secretKeySpec = null;
    try {
      key = dynaKey.getBytes(StandardCharsets.UTF_8);
      sha = MessageDigest.getInstance("SHA-1");
      key = sha.digest(key);
      key = Arrays.copyOf(key, 16);
      secretKeySpec = new SecretKeySpec(key, "AES");
    } catch (NoSuchAlgorithmException nse) {
      logger.error("NoSuchAlgorithmException while creatingKey ", nse);
    }
    return secretKeySpec;
  }

}
