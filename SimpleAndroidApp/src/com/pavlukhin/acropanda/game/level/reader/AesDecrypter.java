package com.pavlukhin.acropanda.game.level.reader;

import android.util.Base64;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by ivan on 30.09.2014.
 */
public class AesDecrypter implements Decrypter {

    private static final String keyStr = "cGH7D8V8IqLuaZ9piZkE1g==";
    private static final String ALGO = "AES";

    @Override
    public byte[] decrypt(byte[] bytes) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(Base64.decode(keyStr, Base64.DEFAULT), ALGO);
            return decrypt(bytes, keySpec);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] decrypt(byte[] bytes, Key key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(key.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(bytes);
    }
}
