package com.frederick.mybolckchain;

import com.google.gson.GsonBuilder;
import java.util.ArrayList;

/**
 *
 * @author frederick
 */
public class Mybolckchain {

    public static ArrayList<Block> blockchain = new ArrayList<>();

    public static void main(String[] args) {
        blockchain.add(new Block("First block in blockchain", "0"));

        blockchain.add(new Block("Second block in blockchain",
                blockchain.get(blockchain.size() - 1).hash));

        blockchain.add(new Block("Third block",
                blockchain.get(blockchain.size() - 1).hash));

        String blockchainJson = new GsonBuilder().setPrettyPrinting()
                .create().toJson(blockchain);
        System.out.println(blockchainJson);

    }
    
    public static boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        
        for (int i = 1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);
            
            //compare registered hash and calculated hash
            if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
                System.out.println("Curren Hashes are not equal!");
                return false;
            }
            
            if (!previousBlock.hash.equals(currentBlock.previousHash)) {
                System.out.println("Previous Hashes are not equal!");
                return false;
            } 
            
        }
        
        return true;
    }


}
