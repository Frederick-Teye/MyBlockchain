package com.frederick.mybolckchain;

import java.security.*;
import java.util.ArrayList;

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

    public ArrayList<TransactionInput> inputs = new ArrayList<>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<>();

    private static int sequence = 0; // a rough count of how many transactions have been generated.

    public Transaction(PublicKey from, PublicKey to,
            float value, ArrayList<TransactionInput> inputs) {
        this.sender = from;
        this.reciepient = to;
        this.value = value;
        this.inputs = inputs;
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


    // Returns true if new transaction could be created
    public boolean processTransaction() {
        if (!verifySignature()) {
            System.out.println("Transaction signature failed to verify");
            return false;
        }

        // Gather transaction inputs (Make sure they are unspent)
        for (TransactionInput i : inputs) {
            i.UTXO = MyBolckchain.UTXOs.get(i.transactionOutputId);
        }

        // Check if transaction is valid
        if (getInputsValue() < MyBolckchain.minimumTransaction) {
            System.out.println("#Transaction Inputs to small: " + getInputsValue());
            return false;
        }

        // Generate transaction outputs
        float leftOver = getInputsValue() - value;  // Get value of inputs then the left over change
        transactionId = calculateHash();
        // Send value to reciepient
        outputs.add(new TransactionOutput(this.reciepient, value, transactionId));
        // Send the leftover change back to sender
        outputs.add(new TransactionOutput(this.sender, leftOver, transactionId));

        // Add outputs to Unspent list
        for (TransactionOutput o : outputs) {
            MyBolckchain.UTXOs.put(o.id, o);
        }

        // Remove transaction inputs from UTXO lists as spent
        for (TransactionInput i : inputs) {
            if (i.UTXO == null) {
                continue;
            }
            MyBolckchain.UTXOs.remove(i.UTXO.id);
        }

        return true;
    }


    // Returns sum of inputs(UTXOs) values
    public float getInputsValue() {
        float total = 0;
        for (TransactionInput i : inputs) {
            if (i.UTXO == null) {
                continue;  // If transaction can't be found, skip it
            }
            total += i.UTXO.value;
        }
        return total;
    }


    // Return sum of outputs
    public float getOutputsValue() {
        float total = 0;
        for (TransactionOutput o : outputs) {
            total += o.value;
        }
        return total;
    }

    // This calculates the transaction hash which will be used as its ID.
    private String calculateHash() {
        sequence++;
        return StringUtil.applySha256(
                StringUtil.getStringFromKey(sender)
                + StringUtil.getStringFromKey(reciepient)
                + Float.toString(value)
                + sequence
        );
    }


}
