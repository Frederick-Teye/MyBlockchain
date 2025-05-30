package com.frederick.mybolckchain;

import java.security.*;
import java.util.ArrayList;
import java.util.Base64;

/**
 *
 * @author frederick
 */
public class Transaction {

    public String transactionId; // hash of the transaction
    public PublicKey sender;
    public PublicKey reciepient;
    public float value;
    public byte[] signature;

    public ArrayList<TrasactionInput> inputs = new ArrayList<>();
    public ArrayList<TrasactionOutput> outputs = new ArrayList<>();

    private static int sequence = 0; // a rough count of how many transactions have been generated.

    public Transaction(PublicKey from, PublicKey to,
            float value, ArrayList<TransactionInput> inputs) {
        this.sender = from;
        this.reciepient = to;
        this.value = value;
        this.inputs = inputs;
    }


    // This calculates the transaction hash which will be used as its ID.
    private String caluclateHash() {
        ++sequence; // Increase sequese to avoid same hash for two transactions
        return StringUtil.applySha256(
                StringUtil.getStringFromKey(sender)
                + StringUtil.getStringFromKey(reciepient)
                + Float.toString(value)
                + sequence
        );
    }


    // Applies ECDSA Signature and returns the result as bytes
    public static byte[] applyECDSASig(PrivateKey privateKey, String input) {
        Signature dsa;
        byte[] output;

        try {
            dsa = Signature.getInstance("ECDSA", "BC");
            dsa.initSign(privateKey);
            byte[] strByte = input.getBytes();
            dsa.update(strByte);
            byte[] realSig = dsa.sign();
            output = realSig;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return output;
    }


    // Verifies a String signature
    public static boolean verifyECDSASig(PublicKey publicKey, String data,
            byte[] signature) {
        try {
            Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
            ecdsaVerify.initVerify(publicKey);
            ecdsaVerify.update(data.getBytes());
            return ecdsaVerify.verify(signature);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static String getStringFromKey(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    // Sings all the data we don't wish to be tampered with.
    public void generateSignature(PrivateKey privateKey) {
        String data = StringUtil.getStringFromKey(sender)
                + StringUtil.getStringFromKey(reciepient)
                + Float.toString(value);
        
        signature = StringUtil.applyECDSASig(privateKey, data);
    }


    // Verifies the data we signed hasn't been tampered with
    public boolean verifySignature() {
        String data = StringUtil.getStringFromKey(sender)
                + StringUtil.getStringFromKey(reciepient)
                + Float.toString(value);
        
        return StringUtil.verifyECDSASig(sender, data, signature);
    }
}
