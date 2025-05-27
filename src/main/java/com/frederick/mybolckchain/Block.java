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

    public Block(String data, String previousHash) {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
    }

}
