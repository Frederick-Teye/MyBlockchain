package com.frederick.mybolckchain;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author frederick
 */
public class Block {

    public String hash;
    public String previousHash;
    public final long timeStamp;
    public int nonce;
    public String merkleRoot;
    public ArrayList<Transaction> transactions = new ArrayList<>();

    public Block(String previousHash) {
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }


    public String calculateHash() {
        String calculatedHash = StringUtil.applySha256(
                previousHash
                + Long.toString(timeStamp)
                + Integer.toString(nonce)
                + merkleRoot
        );
        return calculatedHash;
    }


    public void mineBlock(int difficulty) {
        merkleRoot = StringUtil.getMerkleRoot(transactions);
        // target is a 'difficulty' number of zeros that are a string
        String target = StringUtil.getDificultyString(difficulty);
        // we want to make sure that the hash begins with 'difficulty' number of zeros
        while (!hash.substring(0, difficulty).equals(target)) {
            ++nonce;
            hash = calculateHash();
        }
        System.out.println("Block Mined!!! : " + hash);
    }


    // Add transactions to this block
    public boolean addTransaction(Transaction transaction) {
        // Process transaction and check if valid, unless block is genesis
        // block then ignored.
        if (transaction == null) {
            return false;
        }

        if (!previousHash.equals("0")) {
            if (transaction.processTransaction() != true) {
                System.out.println("Transaction failed to process. Discarded.");
                return false;
            }
        }

        transactions.add(transaction);
        System.out.println("Transaction successfully added to Block");
        return true;
    }


}
