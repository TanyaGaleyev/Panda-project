package com.pavlukhin.acropanda.game.level.reader;

/**
 * Created by ivan on 30.07.2014.
 */
public class StubDecrypter implements Decrypter {
    @Override
    public byte[] decrypt(byte[] bytes) {
        return bytes;
    }
}
