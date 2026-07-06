package com.bugotruco.mishimentor;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class MishiCrypto {
    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int TAG_BIT_LENGTH = 128;
    private static final int IV_BYTE_LENGTH = 12;

    private static SecretKey secretKey;

    /**
     * 🔥 El usuario inyecta su token de acceso aquí al iniciar la app o el asistente.
     * Esto genera la llave AES-256 en memoria de manera volátil.
     */
    public static void inicializarBunker(String tokenUsuario) {
        if (tokenUsuario == null || tokenUsuario.trim().isEmpty()) {
            throw new IllegalArgumentException("El token de acceso del búnker no puede estar vacío.");
        }
        try {
            // Derivamos el token a un hash SHA-256 estable de 32 bytes
            byte[] keyBytes = MessageDigest.getInstance("SHA-256")
                    .digest(tokenUsuario.getBytes(StandardCharsets.UTF_8));
            secretKey = new SecretKeySpec(keyBytes, "AES");
        } catch (Exception e) {
            throw new RuntimeException("Error al inicializar las llaves del búnker", e);
        }
    }

    // Los métodos encrypt() y decrypt() se quedan EXACTAMENTE IGUAL que antes...
    public static String encrypt(String plainText) {
        if (secretKey == null) throw new IllegalStateException("Búnker no inicializado criptográficamente.");
        if (plainText == null || plainText.isEmpty()) return "";
        try {
            byte[] iv = new byte[IV_BYTE_LENGTH];
            new SecureRandom().nextBytes(iv);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_BIT_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
            byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            byte[] combined = new byte[iv.length + cipherText.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(cipherText, 0, combined, iv.length, cipherText.length);
            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            throw new RuntimeException("Error al encriptar propiedad", e);
        }
    }

    public static String decrypt(String cipherTextBase64) {
        if (secretKey == null) throw new IllegalStateException("Búnker no inicializado criptográficamente.");
        if (cipherTextBase64 == null || cipherTextBase64.isEmpty()) return "";
        try {
            byte[] combined = Base64.getDecoder().decode(cipherTextBase64);
            byte[] iv = new byte[IV_BYTE_LENGTH];
            System.arraycopy(combined, 0, iv, 0, iv.length);
            int cipherTextLength = combined.length - IV_BYTE_LENGTH;
            byte[] cipherText = new byte[cipherTextLength];
            System.arraycopy(combined, IV_BYTE_LENGTH, cipherText, 0, cipherTextLength);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_BIT_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);
            byte[] decryptedText = cipher.doFinal(cipherText);
            return new String(decryptedText, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error al desencriptar: Token incorrecto o datos corruptos.", e);
        }
    }
}