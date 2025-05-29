package com.frederick.mybolckchain;

import java.security.*;
import java.security.spec.ECGenParameterSpec;

/**
 *
 * @author frederick
 */
public class Wallet {

    public PrivateKey privateKey;
    public PublicKey publicKey;
    
    public Wallet() {
        generateKeyPair();
    }

    public void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            // Specifies that elliptic curve(prime192v1) defined by the NIST 
            // (National Institute of Standards and Technology) should be used.
            // This curve is widely used and provides good balance between 
            // security and performance.
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");

            // Initialize the key generator and generate a KeyPair
            keyGen.initialize(ecSpec, random);  // prime192v1 is 192-bit key, not 256 bytes
            KeyPair keyPair = keyGen.generateKeyPair();

            // Set the public and private keys from the keyPair
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
