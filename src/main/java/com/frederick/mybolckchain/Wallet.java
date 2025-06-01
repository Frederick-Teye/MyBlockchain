package com.frederick.mybolckchain;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author frederick
 */
public class Wallet {

    public PrivateKey privateKey;
    public PublicKey publicKey;

    // Only UTXOs owned by this wallet.
    public HashMap<String, TransactionOutput> UTXOs = new HashMap<>();

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


    // Returns balance and stores the UTXO's owned by this wallet in this.UTXOs
    public float getBalance() {
        float total = 0;
        for (Map.Entry<String, TransactionOutput> item : MyBolckchain.UTXOs.entrySet()) {
            TransactionOutput UTXO = item.getValue();
            if (UTXO.isMine(publicKey)) {
                // if output belongs to me(if coins belong to me)
                // add it to our list of unspent transactions.
                UTXOs.put(UTXO.id, UTXO);
                total += UTXO.value;
            }
        }
        return total;
    }


    // Generate and returns a new transaction from this wallet.
    public Transaction sendFunds(PublicKey _recipient, float value) {
        if (getBalance() < value) {
            System.out.println("#Not enough funds to send transactions. Transaction Discarded.");
            return null;
        }

        // Create array list of inputs
        ArrayList<TransactionInput> inputs = new ArrayList<>();
        float total = 0;

        for (Map.Entry<String, TransactionOutput> item : UTXOs.entrySet()) {
            TransactionOutput UTXO = item.getValue();
            total += UTXO.value;
            inputs.add(new TransactionInput(UTXO.id));

            if (total > value) {
                break;
            }
        }

        Transaction newTransaction = new Transaction(publicKey, _recipient, value, inputs);
        newTransaction.generateSignature(privateKey);

        for (TransactionInput input : inputs) {
            UTXOs.remove(input.transactionOutputId);
        }

        return newTransaction;
    }


}
