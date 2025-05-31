package com.frederick.mybolckchain;

import java.security.PublicKey;

/**
 *
 * @author frederick
 */
public class TransactionOutput {

    public String id;
    public PublicKey reciepient; // New owner of this coins.
    public float value; // The amount of coins they own
    public String parentTransactionId;  // The id of transaction this output was created in.

    public TransactionOutput(PublicKey reciepient, float value,
            String parentTransactionId) {
        this.reciepient = reciepient;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        this.id = StringUtil.applySha256(
                StringUtil.getStringFromKey(reciepient)
                + Float.toString(value)
                + parentTransactionId
        );
    }


    // Check if coin belongs to you
    public boolean isMine(PublicKey publicKey) {
        return (publicKey == reciepient);
    }


}
