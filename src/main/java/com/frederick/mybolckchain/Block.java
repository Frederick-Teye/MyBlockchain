package com.frederick.mybolckchain;

import java.util.Date;

/**
 *
 * @author frederick
 */
public class Block {

    public String hash;
    public String previousHash;
    public String data;
    private final long timeStamp;
    private int nonce;

    public Block(String data, String previousHash) {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }


    public String calculateHash() {
        String calculatedHash = StringUtil.applySha256(
                previousHash
                + Long.toString(timeStamp)
                + Integer.toString(nonce)
                + data
        );
        return calculatedHash;
    }


    public void mineBlock(int difficulty) {
        // target is a 'difficulty' number of zeros that are a string
        String target = new String(new char[difficulty]).replace('\0', '0');
        // we want to make sure that the hash begins with 'difficulty' number of zeros
        while (!hash.substring(0, difficulty).equals(target)) {
            ++nonce;
            hash = calculateHash();
        }
        System.out.println("Block Mined!!! : " + hash);
    }


}
