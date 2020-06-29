/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack;

/**
 *
 * @author C317
 */
public class Card {
    private int cardHolderNumber;
    private String cardNumFace;
    private String cardSuite;
    private int cardBlackjackValue;
    
    public Card(){
        cardHolderNumber = 0;
        cardNumFace = " ";
        cardSuite = " ";
        cardBlackjackValue = 0;
    }
    
    public Card(int holder, String numFace, String suite, int value){
        this.cardHolderNumber = holder;
        this.cardNumFace = numFace;
        this.cardSuite = suite;
        this.cardBlackjackValue = value;
        
    }
    
    public void setCardHolderNum(String a){
        this.cardHolderNumber = Integer.parseInt(a);
    }
    
    public void setCardNumFace(String a){
        this.cardNumFace = a;
    }
    
    public void setCardSuite(String a){
        this.cardSuite = a;
    }
    
    public void setCardBlackjackValue(String a){
        this.cardBlackjackValue = Integer.parseInt(a);
    }
    
    public String getNumFace(){
        return this.cardNumFace;
    }
    
    public String getCardSuite(){
        return this.cardSuite;
    }
    
    public int getBlackjackValue(){
        return this.cardBlackjackValue;
    }
    
    public void printCard(){
        System.out.print(cardHolderNumber + " " + cardNumFace + " " + cardSuite + " " + cardBlackjackValue);
    }
    
}




