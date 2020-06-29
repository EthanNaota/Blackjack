/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Ethan Naota
 */
public class Blackjack {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int decklocation = -1;
        
        Player dealer = new Player(true, 0, "Dealer", 0);
        Player player1 = new Player();

        //introduce the game/rules and set player's class
        
        try{
        grabPlayersWallet(player1);
        }
        catch(FileNotFoundException e){
            System.out.println();
            System.out.println("-----------------------------------------------------");
            System.out.println(e.getMessage());
            System.out.println("Please check PLAYERMONEY File Name and try again.");
            System.out.println("-----------------------------------------------------");                
            System.out.println();
            System.exit(0);
        }
        
        hello(player1);
        player1.setPlayerNumber(1);
        
        //create deck of cards
        Card[] deck = null;

        try{
            deck = setDeck();
        }
        catch (FileNotFoundException e){
            System.out.println();
            System.out.println("-----------------------------------------------------");
            System.out.println(e.getMessage());
            System.out.println("Please check DECK File Name and try again.");
            System.out.println("-----------------------------------------------------");                
            System.out.println();
            System.exit(0);
        }
        
//        for (int i=0; i < 51 + 1; i++){
//            deck[i].printCard();
//            System.out.println();        
//        }        

        shuffle(deck);
        
//        for (int i=0; i < 51 + 1; i++){
//            deck[i].printCard();
//            System.out.println();        
//        }            
        
        //Start of Game
        double potOfGame = 0;
        int playerTotalScore = 0;
        int dealerTotalScore = 0;
        boolean blackjackHit = false;
        boolean bust = false;
        boolean stick = false;
        boolean dealerBlackjackHit = false;
        boolean dealerBust = false;
        boolean dealerStick = false;
        boolean gameLoop = true;
        
        while (gameLoop){
            potOfGame = anteUp(player1);


            decklocation = grabbingCard(decklocation, deck);
            Card card1 = deck[decklocation];
            displayCard(player1, card1);

            decklocation = grabbingCard(decklocation, deck);        
            Card card2 = deck[decklocation];
            displayCard(dealer, card2);

            decklocation = grabbingCard(decklocation, deck);         
            Card card3 = deck[decklocation];
            displayCard(player1, card3);

            decklocation = grabbingCard(decklocation, deck);         
            Card card4 = deck[decklocation];
            dealerHiddenCard();

            playerTotalScore = card1.getBlackjackValue() + card3.getBlackjackValue();
            potOfGame = potOfGame + bet(player1, playerTotalScore, potOfGame);

            blackjackHit = hitBlackjackOrNot(playerTotalScore);
            bust = bustOrNot(playerTotalScore);        

            //players turn
            while (!blackjackHit && !bust && !stick){
                stick = stickOrHit(playerTotalScore);

                if(!stick){
                    decklocation = grabbingCard(decklocation, deck);
                    displayCard(player1, deck[decklocation]);

                    playerTotalScore = addScore(playerTotalScore, deck[decklocation].getBlackjackValue());

                    blackjackHit = hitBlackjackOrNot(playerTotalScore);
                    bust = bustOrNot(playerTotalScore);
                } //end when player wants to stick, busts or hits blackjack    
            } //while no blackjack, no bust, no stick
            
            System.out.println("-----------------------------------------------------");
            System.out.println();
            
            //dealers turn
            if (!bust){
                dealerTotalScore = dealersInitalTurn(dealer, card2, card4);

                dealersTotalScoreDisplayed(dealerTotalScore);
                dealerBlackjackHit = dealerHitBlackjackOrNot(dealerTotalScore);
                dealerBust = dealerBustOrNot(dealerTotalScore);
                dealerStick = dealerStickOrHit(dealerTotalScore);

                pauseDealerTurn();

                while(!dealerBlackjackHit && !dealerBust && !dealerStick){
                    while (!dealerStick){
                        decklocation = grabbingCard(decklocation, deck);
                        displayCard(dealer, deck[decklocation]);

                        dealerTotalScore = addScore(dealerTotalScore, deck[decklocation].getBlackjackValue());
                        dealersTotalScoreDisplayed(dealerTotalScore);

                        dealerStick = dealerStickOrHit(dealerTotalScore);
                    } //ends when dealer sticks

                    dealerBlackjackHit = dealerHitBlackjackOrNot(dealerTotalScore);
                    dealerBust = dealerBustOrNot(dealerTotalScore);
                } //ends when dealer hits blackjack, busts or sticks          
            } //end if player didnt bust, dealers turn

            //calculate winnings
            winnings(bust, playerTotalScore, player1, dealerBust, dealerBlackjackHit, dealerTotalScore, potOfGame);

            //save players wallet amount
            try{
                saveMoneyToFile(player1);
            }
            catch (FileNotFoundException e){
                System.out.println(e.getMessage());
                System.exit(1);  
            }
            //reset game
            potOfGame = 0;
            playerTotalScore = 0;
            dealerTotalScore = 0;
            blackjackHit = false;
            bust = false;
            stick = false;
            dealerBlackjackHit = false;
            dealerBust = false;
            dealerStick = false;
            
            Scanner kb = new Scanner(System.in);
            kb.nextLine();
        }
    }
    
    public static void pauseDealerTurn(){
        System.out.println("(PRESS ENTER TO CONTINUE DEALERS TURN)");
        Scanner kb = new Scanner(System.in);
        kb.nextLine();
    }

    public static void saveMoneyToFile(Player a) throws FileNotFoundException{
        java.io.File x = new java.io.File("PlayerMoney.txt");
        java.io.PrintWriter y = new java.io.PrintWriter(x);
        y.print(a.getPlayerMoney());
        y.close();
    }

    // commit test
    
    public static void grabPlayersWallet(Player a) throws FileNotFoundException{
        Scanner infile;
        File dataFile = new File ("Blackjack\\Blackjack\\PlayerMoney.txt");
        infile = new Scanner (dataFile);
        double money = 0f;
        
        money = Double.parseDouble(infile.nextLine());
        a.setPlayerMoney(money);
    }
    
    public static void winnings(boolean playerBust, int playerScore, Player a, boolean dealerBust, boolean dealerBlackjack, int dealerScore, double bet){
        double winnings = bet * 2;
        
        if (dealerBlackjack){
            System.out.println("Dealer automatically wins for hitting BLACKJACK.");
            System.out.println("You lose the round. Play Again!");
            System.out.println();
            
            System.out.print("You have $");
            System.out.printf("%.2f", a.getPlayerMoney());
            System.out.println(" left in your wallet.");
            System.out.println("(PRESS ENTER TO CONTINUE TO NEXT ROUND.)");
            System.out.println();
        }
        else if (dealerBust){
            System.out.println("Dealer automatically loses for BUST.");
            System.out.println("You win the round. Play Again!");
            System.out.println();
            
            a.setPlayerMoney(a.getPlayerMoney() + winnings);
            System.out.println("You won: $" + winnings);
            System.out.print("You have $");
            System.out.printf("%.2f", a.getPlayerMoney());
            System.out.println(" left in your wallet.");
            System.out.println("(PRESS ENTER TO CONTINUE TO NEXT ROUND.)");
            System.out.println();
        }
        else if (playerBust){
            System.out.print("You have $");
            System.out.printf("%.2f", a.getPlayerMoney());            
            System.out.println(" left in your wallet.");
            System.out.println("(PRESS ENTER TO CONTINUE TO NEXT ROUND.)");
            System.out.println();
        }
        else if (playerScore > dealerScore){
            System.out.println(a.getPlayerName() + " ended the round with a card score of " + playerScore + "!");
            System.out.println("Dealer ended the round with a card score of " + dealerScore + ".");
            System.out.println("You beat the dealer this round!");
            System.out.println();
            
            a.setPlayerMoney(a.getPlayerMoney() + winnings);
            System.out.println("You won: $" + winnings);
            System.out.println("You have $" + a.getPlayerMoney() + " left in your wallet.");
            System.out.println("(PRESS ENTER TO CONTINUE TO NEXT ROUND.)");
            System.out.println();            
        }
        else if (dealerScore >= playerScore){
            System.out.println(a.getPlayerName() + " ended the round with a card score of " + playerScore + "!");
            System.out.println("Dealer ended the round with a card score of " + dealerScore + ".");
            System.out.println("The dealer wins this round!");
            System.out.println();
            
            System.out.println("You have $" + a.getPlayerMoney() + " left in your wallet.");
            System.out.println("(PRESS ENTER TO CONTINUE TO NEXT ROUND.)");
            System.out.println();            
        }
    }
    
    public static void dealersTotalScoreDisplayed(int dealerScore){
        System.out.println("Dealer's card total is: " + dealerScore);
        System.out.println();
    }
    
    public static int dealersInitalTurn(Player dealer, Card card1, Card card2){
        int dealerTotal;
        
        System.out.println("DEALERS TURN");
        System.out.println();
        
        displayCard(dealer, card1);
        displayCard(dealer, card2);
        
        dealerTotal = card1.getBlackjackValue() + card2.getBlackjackValue();

        return dealerTotal;
    }
    
    public static int addScore(int totalScore, int newCard){
        return totalScore + newCard;
    }
    
    public static boolean dealerStickOrHit(int cardTotal){
        if (cardTotal <= 17){
            System.out.println("Dealer Hits.");
            System.out.println();
            return false;
        }
        if (cardTotal <= 21){
            System.out.println("Dealer Sticks.");
            System.out.println();
            return true;
        }
        return true;
        
    }
    
    public static boolean stickOrHit(int cardTotal){
        Scanner kb = new Scanner(System.in);
        boolean loop = true;
        
        while (loop){
            System.out.println("Your cards total value is: " + cardTotal);
            System.out.println();
            System.out.println("Would you like to Hit or Stick?");
            System.out.print("Enter 'HIT' or 'STICK': ");
            String hitOrStick = kb.nextLine();

            if(hitOrStick.equalsIgnoreCase("stick")){
                System.out.println();
                System.out.println("Turn passes to dealer.");
                System.out.println();                
                return true;
            } //end if
            if(hitOrStick.equalsIgnoreCase("hit")){
                System.out.println();
                return false;
            } //end if
        System.out.println();            
        System.out.println("Please enter 'HIT' or 'STICK' to continue.");
        System.out.println();
        }//end while loop
        
        return true;        
        
    }

    public static boolean bustOrNot (int cardTotal){
        if (cardTotal > 21){
            System.out.println("BUST! (Cards Total is over 21)");
            System.out.println("You lose the round.");
            System.out.println();
            return true;
        }
        return false;
    }

    public static boolean dealerBustOrNot (int cardTotal){
        if (cardTotal > 21){
            System.out.println("Dealer busts! (Cards Total is over 21)");
            System.out.println("Dealer loses the round. Play Again!");
            System.out.println();
            return true;
        }
        return false;
    }    

    public static boolean dealerHitBlackjackOrNot (int cardTotal){
        if (cardTotal == 21){
            System.out.println("DEALER HIT BLACKJACK!");
            System.out.println("Dealer wins the round.");
            System.out.println();
            return true;
        }
        return false;
    }
    
    public static boolean hitBlackjackOrNot (int cardTotal){
        if (cardTotal == 21){
            System.out.println("YOU HIT BLACKJACK!");
            System.out.println("Passes to dealer's turn.");
            System.out.println();
            
            System.out.println("(PRESS ENTER TO CONTINUE DEALERS TURN)");
            System.out.println();
            Scanner kb = new Scanner(System.in);
            kb.nextLine();
            
            return true;
        }
        return false;
    }
    
    public static double bet(Player a, int totalScore, double pot) throws NumberFormatException{
        Scanner kb = new Scanner(System.in);
        boolean loop = true;
        boolean loop2 = true;
        double betAmount = 0;
        
        while (loop){
            System.out.println("Your total card's scores value is " + totalScore);
            System.out.println("Given the cards you drew would you like to increase your bet amount?");
            System.out.print("Enter 'YES' or 'NO': ");
            String yesOrNo = kb.nextLine();

            if(yesOrNo.equalsIgnoreCase("no")){
                System.out.println();
                return 0;
            } //end if
            if(yesOrNo.equalsIgnoreCase("yes")){
                while(loop2){
                    System.out.println();
                    System.out.println("How much would you like to increase your bet amount by?");
                    System.out.print("You have $");
                    System.out.printf("%.2f", a.getPlayerMoney());                    
                    System.out.println(" in your wallet.");
                    System.out.print("Enter Amount: $");

                    betAmount = enterAmount();
                    
                    if (betAmount > a.getPlayerMoney() || betAmount < 0){
                        System.out.println();            
                        System.out.println("You cannot bet that amount please try again.");
                    } //end if
                    else {
                        loop2 = false;
                        double newPot = betAmount + pot;
                        a.setPlayerMoney(a.getPlayerMoney() - betAmount);
                        
                        System.out.print("You are betting $");
                        System.out.printf("%.2f", newPot);                                
                        System.out.println(" on this hand. Good Luck!");
                        System.out.println();
                        return betAmount;
                    }
                } //end loop2    
            } //end if
        System.out.println();            
        System.out.println("Please enter 'Yes' or 'No' to continue.");
        System.out.println();
        }//end while loop
        
        return 0;
    }
    
    public static double enterAmount() throws NumberFormatException{
        int betAmount = 0;
        boolean retry = true;
        
        while (retry){
            Scanner kb = new Scanner(System.in);
            String bet = kb.nextLine();
            
            try{
                betAmount = Integer.parseInt(bet);
                return betAmount;
            } //end try
            catch (NumberFormatException e){
                System.out.println();
                System.out.println("Not a Real Number Entered. Please Try Again.");
                System.out.println();
                System.out.print("Enter Amount: $");
            }
        }
        return 0;
    }
        
    public static double anteUp(Player a){
        if(a.getPlayerMoney()>= 5d){
            System.out.println(a.getPlayerName() + " has deposited $5 for the ante of this round!");
            System.out.println();
            
            a.setPlayerMoney(a.getPlayerMoney() - 5);
            return 5f;
        }
        else{
            System.out.println(a.getPlayerName() + " you do not have enough money in your account to play this round.");
            System.out.println("You have been given an additional $100 to continue playing! Don't Make Us Break Ya Knee Caps!");
            System.out.println();
            
            a.setPlayerMoney(a.getPlayerMoney() + 95);            
            return 5f;
        }
    }
    
    public static void dealerHiddenCard(){
        System.out.println("Dealer's second drawn card is hidden until dealer's turn.");
        System.out.println();
    }
    
    public static void displayCard(Player b, Card a){
        char spade = '\u2660';
        char club = '\u2663';
        char heart = '\u2661';
        char diamond = '\u2662';
        
        if (a.getCardSuite().equalsIgnoreCase("spade")){
            System.out.println(" - " + b.getPlayerName() + " was dealt: " + a.getNumFace() + spade);
            System.out.println();
        }
        if (a.getCardSuite().equalsIgnoreCase("club")){
            System.out.println(" - " + b.getPlayerName() + " was dealt: " + a.getNumFace() + club);
            System.out.println();
        }
        if (a.getCardSuite().equalsIgnoreCase("heart")){
            System.out.println(" - " + b.getPlayerName() + " was dealt: " + a.getNumFace() + heart);
            System.out.println();
        }
        if (a.getCardSuite().equalsIgnoreCase("diamond")){
            System.out.println(" - " + b.getPlayerName() + " was dealt: " + a.getNumFace() + diamond);
            System.out.println();
        }
    }
    
    public static int grabbingCard(int a, Card[] cardArray){
        a++;
        if (a < 52){       
            System.out.println("DRAWING CARD");
            System.out.println();        
            return a;
        }
        else{
            System.out.println();
            System.out.println("DECK OUT OF CARDS");
            shuffle(cardArray);

            System.out.println();        
            System.out.println("DRAWING CARD");
            System.out.println();
            
            return a= 0;
        }
    }
    
    public static Card[] setDeck() throws FileNotFoundException{
        Card[] deckOfCards = new Card[52];
        
        Scanner infile;
        File dataFile = new File ("Blackjack\\Blackjack\\CardsInfo.txt");
        infile = new Scanner (dataFile);
        
        for (int i=0; i < 52; i++){
            deckOfCards[i] = new Card();
            deckOfCards[i].setCardHolderNum(infile.nextLine());
            deckOfCards[i].setCardNumFace(infile.nextLine());
            deckOfCards[i].setCardBlackjackValue(infile.nextLine());
            deckOfCards[i].setCardSuite(infile.nextLine());
        }
        return deckOfCards;
    }
    
    public static void hello(Player a){
        System.out.println("Hello and Welcome to a game of Blackjack.");
        System.out.println("It's you against the dealer!");
        System.out.println();
        System.out.println("The rules are simple:");
        System.out.println("You are dealt two cards and need to get to 21 or as close as possible.");
        System.out.println("Just like a normal game of blackjack against a dealer you have to beat ");
        System.out.println("the dealer's hand in order to win the pot.");
        System.out.println("(If you tied with the dealer you lose the hand!)");
        System.out.println("All face cards are worth 10 points.");
        System.out.println("All Aces are are worth 11 points for the purpose for this game");
        System.out.println("All other cards are worth their number face value");
        System.out.println();
        System.out.println("What is your name?");
        System.out.print("Enter Name: ");
        
        Scanner kb = new Scanner(System.in);
        a.setPlayerName(kb.nextLine()); 
        
        System.out.println();
        System.out.print("Good luck " + a.getPlayerName() + "! You have $");
        System.out.printf("%.2f", a.getPlayerMoney());
        System.out.println(" left from last round to get you started!");
    }
    
    
    
    public static void shuffle(Card[] theArray) {
        System.out.println();
        System.out.println("SHUFFLING DECK");
        System.out.println();
        
        Card placeholder = new Card();
        
        for (int i = theArray.length - 1; i >= 1; i--) {
            Random rand = new Random();
            // Generate random number between 0 and i
            // We need to use i + 1 in the call to nextInt because
            // nextInt returns a value between 0 and the specified
            // number excluding the number.
            int j = rand.nextInt(i + 1);

            // Exchange the data from the array's i and j indexes.
            

            placeholder = theArray[j];
            theArray[j] = theArray[i];
            theArray[i] = placeholder;
        }    
    }
}
