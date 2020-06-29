/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack;

/**
 *
 * @author Naota
 */
public class Player {
    private boolean dealerOrPlayer;
    private int playerNumber;
    private String playerName;
    private double playerMoney;
    
    public Player(){
        dealerOrPlayer = false;
        playerNumber = 0;
        playerName = " ";
        playerMoney = 0;
    }

    public Player(boolean dealerPlayer, int number, String name, double money){
        this.dealerOrPlayer = dealerPlayer;
        this.playerNumber = number;
        this.playerName = name;
        this.playerMoney = money;
    }

    public boolean getDealerorPlayer(){
        return this.dealerOrPlayer;
    }

    public void setDealerOrPlayer(boolean a){
        this.dealerOrPlayer = a;
    }

    public int getPlayerNumber(){
        return this.playerNumber;
    }

    public void setPlayerNumber(int a){
        this.playerNumber = a;
    }
    public String getPlayerName(){
        return this.playerName;
    }

    public void setPlayerName(String a){
        this.playerName = a;
    }

    public double getPlayerMoney(){
        return this.playerMoney;
    }

    public void setPlayerMoney(double a){
        this.playerMoney = a;
    }

}
