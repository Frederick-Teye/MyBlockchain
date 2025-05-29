package com.frederick.mybolckchain;

import com.google.gson.GsonBuilder;
import java.util.ArrayList;

/**
 *
 * @author frederick
 */
public class Mybolckchain {

    public static ArrayList<Block> blockchain = new ArrayList<>();
    public static int difficulty = 6;

    public static void main(String[] args) {
        blockchain.add(new Block("First block in blockchain", "0"));
        System.out.println("Trying to Mine block 1...");
        blockchain.get(0).mineBlock(difficulty);

        blockchain.add(new Block("Second block in blockchain",
                blockchain.get(blockchain.size() - 1).hash));
        System.out.println("\nTrying to Mine block 2...");
        blockchain.get(1).mineBlock(difficulty);

        blockchain.add(new Block("Third block",
                blockchain.get(blockchain.size() - 1).hash));
        System.out.println("\nTrying to Mine block 3...");
        blockchain.get(2).mineBlock(difficulty);
        
        System.out.println("\nIs Blockchain valid? \nAnswer: " + isChainValid());

        String blockchainJson = new GsonBuilder().setPrettyPrinting()
                .create().toJson(blockchain);
        System.out.println("\n\nThe block chain: ");
        System.out.println(blockchainJson);

    }
    
    public static boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
        
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
            
            // check if has hasn't been solved
            if (!currentBlock.hash.substring(0, difficulty).equals(hashTarget)) {
                System.out.println("This block hasn't been mined");
                return false;
            }
        }
        
        return true;
    }


}
