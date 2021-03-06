/*
 * Tic Tac Toe 3x3 in Java
 * Copyright (c) 2015 Alexander Balgavy <a.balgavy@gmail.com>
 * Distributed under the WTFNMF Public License
 *
 * A game of Tic-Tac-Toe on a 3-by-3 grid.
 * Singleplayer (hard/easy AI) and multiplayer mode
 *
 * Length: 375 lines total
 */

package tic.tac.toe;

import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Alex Balgavy
 */
public class TicTacToe {

    public static void main(String[] args) {
        boolean playAgain = true;
        while (playAgain) {
            // Set up class instances
            Scanner in = new Scanner(System.in);

            // Variables
            // curP is the current piece being used (x or o)
            boolean win = false;
            boolean tie = false;
            boolean ai = false;
            String difficulty;
            char curP;
            char winP = 'x';
            System.out.println("Welcome to Tic-Tac-Toe!");

            // Number of players
            int numPlayers = 0;
            while (numPlayers != 1 && numPlayers != 2) {

                System.out.print("How many players? (1 or 2) ");
                numPlayers = in.nextInt();
                if (numPlayers != 1 && numPlayers != 2) {
                    System.out.println("Wrong entry.");
                }
            }

            // If 1 player: choose AI
            // In while loop to guarantee selection
            boolean aiset = false;
            AI myAI = new AI();
            while (aiset == false) {
                if (numPlayers == 1) {
                    ai = true;
                    System.out.print("Easy? ");
                    difficulty = in.next();
                    
                    // Easy difficulty
                    if (difficulty.contains("y")) {
                        myAI.setDifficulty('e');
                        aiset = true;
                    }
                    // Hard difficulty
                    else if (difficulty.contains("n")) {
                        myAI.setDifficulty('h');
                        aiset = true;
                    } 
                    else {
                        System.out.println("Invalid entry, choose 'y' or 'n'.");
                    }
                    
                } else if (numPlayers == 2) {
                    ai = false;
                    aiset = true;
                } else {
                    System.out.println("Invalid entry.");
                }
            }

            if (ai == true && numPlayers == 1) {
                Random random = new Random();
                curP = (random.nextInt() % 2 == 0) ? 'x' : 'o';
            } else {
                curP = 'x';
            }
            
            // Begin game
            Board board = new Board();

            // If AI goes first, if it was selected
            // If player was chosen to go first, this does not run
            if (curP == 'o' && ai && numPlayers == 1) {
                System.out.println("AI goes first");

                // Make sure a move occurs
                boolean moved = false;
                while (moved == false) {
                    int[] coordinates;

                    // If easy AI, generate random coordinates
                    if (myAI.getDifficulty() == 'e') {
                        coordinates = myAI.generateCoordinates();
                    } // If hard, randomly choose one of four corners
                    else {
                        Random rand = new Random();
                        int randCorner = rand.nextInt(4) + 1;
                        if (randCorner == 1) {
                            coordinates = new int[]{0, 0};
                        } else if (randCorner == 2) {
                            coordinates = new int[]{2, 0};
                        } else if (randCorner == 3) {
                            coordinates = new int[]{0, 2};
                        } else {
                            coordinates = new int[]{2, 2};
                        }
                    }

                    // Make the move
                    if (board.move('o', coordinates[0], coordinates[1])) {
                        moved = true;
                    } else {
                        moved = false;
                    }
                }

                switch (curP) {
                    case 'x':
                        curP = 'o';
                        break;
                    default:
                        curP = 'x';
                        break;

                }

            }

            // While there is a free space and nobody's won
            while (!win && !tie) {
                // Show the board to the user
                board.print();
                System.out.println("Turn: " + curP);

                // Collect user input from 'x,y' form
                // Split it into an array
                System.out.print("Enter x,y coordinates: ");
                String[] newMoveString = in.next().split(",");

                // Must convert to int to use in board[][] array
                int xInt = Integer.parseInt(newMoveString[0]);
                int yInt = Integer.parseInt(newMoveString[1]);

                // If you can make this move
                if (board.move(curP, xInt, yInt)) {

                    // Check for win & tie right at the beginning
                    // Must be done right after move was made, before next turn
                    if (board.isWinner(curP)) {
                        win = true;
                        winP = curP;
                    }
                    if (board.isTied()) {
                        tie = true;
                    }

                    // AI's move (if there is AI)
                    if (numPlayers == 1 && !win && !tie) {

                        // Make sure a move is made
                        boolean moved = false;
                        while (moved == false) {
                            int[] coordinates;

                            // Easy AI: generate coordinates
                            if (myAI.getDifficulty() == 'e') {
                                coordinates = myAI.generateCoordinates();
                            } 

                            // Hard AI: do other stuff
                            else {
                                // Check for center space free
                                if (board.getSpace(1, 1) == ' ') {
                                    coordinates = new int[]{1, 1};
                                } // Row 0
                                else if (board.getSpace(0, 0) == 'x' && board.getSpace(2, 0) == 'x' && board.getSpace(1, 0) == ' ') {
                                    coordinates = new int[]{1, 0};
                                } else if (board.getSpace(1, 0) == 'x' && board.getSpace(2, 0) == 'x' && board.getSpace(0, 0) == ' ') {
                                    coordinates = new int[]{0, 0};
                                } else if (board.getSpace(0, 0) == 'x' && board.getSpace(1, 0) == 'x' && board.getSpace(2, 0) == ' ') {
                                    coordinates = new int[]{2, 0};
                                } // Row 1
                                else if (board.getSpace(0, 1) == 'x' && board.getSpace(2, 1) == 'x' && board.getSpace(1, 1) == ' ') {
                                    coordinates = new int[]{1, 1};
                                } else if (board.getSpace(1, 1) == 'x' && board.getSpace(2, 1) == 'x' && board.getSpace(0, 1) == ' ') {
                                    coordinates = new int[]{0, 1};
                                } else if (board.getSpace(0, 1) == 'x' && board.getSpace(1, 1) == 'x' && board.getSpace(2, 1) == ' ') {
                                    coordinates = new int[]{2, 1};
                                } // Row 2
                                else if (board.getSpace(0, 2) == 'x' && board.getSpace(2, 2) == 'x' && board.getSpace(1, 2) == ' ') {
                                    coordinates = new int[]{1, 2};
                                } else if (board.getSpace(1, 2) == 'x' && board.getSpace(2, 2) == 'x' && board.getSpace(0, 2) == ' ') {
                                    coordinates = new int[]{0, 2};
                                } else if (board.getSpace(0, 2) == 'x' && board.getSpace(1, 2) == 'x' && board.getSpace(2, 2) == ' ') {
                                    coordinates = new int[]{2, 2};
                                } // Column 0
                                else if (board.getSpace(0, 0) == 'x' && board.getSpace(0, 2) == 'x' && board.getSpace(0, 1) == ' ') {
                                    coordinates = new int[]{0, 1};
                                } else if (board.getSpace(0, 1) == 'x' && board.getSpace(0, 2) == 'x' && board.getSpace(0, 0) == ' ') {
                                    coordinates = new int[]{0, 0};
                                } else if (board.getSpace(0, 0) == 'x' && board.getSpace(0, 1) == 'x' && board.getSpace(0, 2) == ' ') {
                                    coordinates = new int[]{0, 2};
                                } // Column 1
                                else if (board.getSpace(1, 0) == 'x' && board.getSpace(1, 2) == 'x' && board.getSpace(1, 1) == ' ') {
                                    coordinates = new int[]{1, 1};
                                } else if (board.getSpace(1, 1) == 'x' && board.getSpace(1, 2) == 'x' && board.getSpace(1, 0) == ' ') {
                                    coordinates = new int[]{1, 0};
                                } else if (board.getSpace(1, 0) == 'x' && board.getSpace(1, 1) == 'x' && board.getSpace(1, 2) == ' ') {
                                    coordinates = new int[]{1, 2};
                                } // Column 2
                                else if (board.getSpace(2, 0) == 'x' && board.getSpace(2, 2) == 'x' && board.getSpace(2, 1) == ' ') {
                                    coordinates = new int[]{2, 1};
                                } else if (board.getSpace(2, 1) == 'x' && board.getSpace(2, 2) == 'x' && board.getSpace(2, 0) == ' ') {
                                    coordinates = new int[]{2, 0};
                                } else if (board.getSpace(2, 0) == 'x' && board.getSpace(2, 1) == 'x' && board.getSpace(2, 2) == ' ') {
                                    coordinates = new int[]{2, 2};
                                } // Diagonal 1
                                else if (board.getSpace(0, 0) == 'x' && board.getSpace(2, 2) == 'x' && board.getSpace(1, 1) == ' ') {
                                    coordinates = new int[]{1, 1};
                                } else if (board.getSpace(0, 0) == 'x' && board.getSpace(1, 1) == 'x' && board.getSpace(2, 2) == ' ') {
                                    coordinates = new int[]{2, 2};
                                } else if (board.getSpace(1, 1) == 'x' && board.getSpace(2, 2) == 'x' && board.getSpace(0, 0) == ' ') {
                                    coordinates = new int[]{0, 0};
                                } // Diagonal 2
                                else if (board.getSpace(0, 2) == 'x' && board.getSpace(2, 0) == 'x' && board.getSpace(1, 1) == ' ') {
                                    coordinates = new int[]{1, 1};
                                } else if (board.getSpace(0, 2) == 'x' && board.getSpace(1, 1) == 'x' && board.getSpace(2, 0) == ' ') {
                                    coordinates = new int[]{2, 0};
                                } else if (board.getSpace(1, 1) == 'x' && board.getSpace(2, 0) == 'x' && board.getSpace(0, 2) == ' ') {
                                    coordinates = new int[]{0, 2};
                                } // If there are no two of same kind
                                // Row 0
                                else if (board.getSpace(0, 0) == 'o' && board.getSpace(2, 0) == 'o' && board.getSpace(1, 0) == ' ') {
                                    coordinates = new int[]{1, 0};
                                } else if (board.getSpace(1, 0) == 'o' && board.getSpace(2, 0) == 'o' && board.getSpace(0, 0) == ' ') {
                                    coordinates = new int[]{0, 0};
                                } else if (board.getSpace(0, 0) == 'o' && board.getSpace(1, 0) == 'o' && board.getSpace(2, 0) == ' ') {
                                    coordinates = new int[]{2, 0};
                                } // Row 1
                                else if (board.getSpace(0, 1) == 'o' && board.getSpace(2, 1) == 'o' && board.getSpace(1, 1) == ' ') {
                                    coordinates = new int[]{1, 1};
                                } else if (board.getSpace(1, 1) == 'o' && board.getSpace(2, 1) == 'o' && board.getSpace(0, 1) == ' ') {
                                    coordinates = new int[]{0, 1};
                                } else if (board.getSpace(0, 1) == 'o' && board.getSpace(1, 1) == 'o' && board.getSpace(2, 1) == ' ') {
                                    coordinates = new int[]{2, 1};
                                } // Row 2
                                else if (board.getSpace(0, 2) == 'o' && board.getSpace(2, 2) == 'o' && board.getSpace(1, 2) == ' ') {
                                    coordinates = new int[]{1, 2};
                                } else if (board.getSpace(1, 2) == 'o' && board.getSpace(2, 2) == 'o' && board.getSpace(0, 2) == ' ') {
                                    coordinates = new int[]{0, 2};
                                } else if (board.getSpace(0, 2) == 'o' && board.getSpace(1, 2) == 'o' && board.getSpace(2, 2) == ' ') {
                                    coordinates = new int[]{2, 2};
                                } // Column 0
                                else if (board.getSpace(0, 0) == 'o' && board.getSpace(0, 2) == 'o' && board.getSpace(0, 1) == ' ') {
                                    coordinates = new int[]{0, 1};
                                } else if (board.getSpace(0, 1) == 'o' && board.getSpace(0, 2) == 'o' && board.getSpace(0, 0) == ' ') {
                                    coordinates = new int[]{0, 0};
                                } else if (board.getSpace(0, 0) == 'o' && board.getSpace(0, 1) == 'o' && board.getSpace(0, 2) == ' ') {
                                    coordinates = new int[]{0, 2};
                                } // Column 1
                                else if (board.getSpace(1, 0) == 'o' && board.getSpace(1, 2) == 'o' && board.getSpace(1, 1) == ' ') {
                                    coordinates = new int[]{1, 1};
                                } else if (board.getSpace(1, 1) == 'o' && board.getSpace(1, 2) == 'o' && board.getSpace(1, 0) == ' ') {
                                    coordinates = new int[]{1, 0};
                                } else if (board.getSpace(1, 0) == 'o' && board.getSpace(1, 1) == 'o' && board.getSpace(1, 2) == ' ') {
                                    coordinates = new int[]{1, 2};
                                } // Column 2
                                else if (board.getSpace(2, 0) == 'o' && board.getSpace(2, 2) == 'o' && board.getSpace(2, 1) == ' ') {
                                    coordinates = new int[]{2, 1};
                                } else if (board.getSpace(2, 1) == 'o' && board.getSpace(2, 2) == 'o' && board.getSpace(2, 0) == ' ') {
                                    coordinates = new int[]{2, 0};
                                } else if (board.getSpace(2, 0) == 'o' && board.getSpace(2, 1) == 'o' && board.getSpace(2, 2) == ' ') {
                                    coordinates = new int[]{2, 2};
                                } // Diagonal 1
                                else if (board.getSpace(0, 0) == 'o' && board.getSpace(2, 2) == 'o' && board.getSpace(1, 1) == ' ') {
                                    coordinates = new int[]{1, 1};
                                } else if (board.getSpace(0, 0) == 'o' && board.getSpace(1, 1) == 'o' && board.getSpace(2, 2) == ' ') {
                                    coordinates = new int[]{2, 2};
                                } else if (board.getSpace(1, 1) == 'o' && board.getSpace(2, 2) == 'o' && board.getSpace(0, 0) == ' ') {
                                    coordinates = new int[]{0, 0};
                                } // Diagonal 2
                                else if (board.getSpace(0, 2) == 'o' && board.getSpace(2, 0) == 'o' && board.getSpace(1, 1) == ' ') {
                                    coordinates = new int[]{1, 1};
                                } else if (board.getSpace(0, 2) == 'o' && board.getSpace(1, 1) == 'o' && board.getSpace(2, 0) == ' ') {
                                    coordinates = new int[]{2, 0};
                                } else if (board.getSpace(1, 1) == 'o' && board.getSpace(2, 0) == 'o' && board.getSpace(0, 2) == ' ') {
                                    coordinates = new int[]{0, 2};
                                } // If there are no two of same kind
                                else {
                                    coordinates = myAI.generateCoordinates();
                                }

                            }

                            // If the move can be made
                            if (board.move('o', coordinates[0], coordinates[1])) {
                                moved = true;

                                // Check if AI won
                                if (board.isWinner('o')) {
                                    win = true;
                                    winP = 'o';

                                }

                                // Check if tie
                                if (board.isTied()) {
                                    tie = true;
                                }
                            } else {
                                moved = false;
                            }
                        }
                    } // If no AI, switch player
                    else {
                        if (!win && !tie) {
                            switch (curP) {
                                case 'x':
                                    curP = 'o';
                                    break;
                                default:
                                    curP = 'x';
                                    break;

                            }
                        }
                    }

                } // Player cannot make this move
                // Happens if space is occupied or doesn't exist
                else {
                    System.out.println("!!! INVALID MOVE !!!");
                    System.out.println("Go again.");
                }

            }

            // Print out the board
            board.print();

            // Show a message depending on what condition occurred
            if (win == true) {
                if (ai && numPlayers == 1 && winP == 'o') {
                    System.out.println("AI WON!");
                } else {
                    System.out.println("YOU WON!");
                }
            } else if (tie == true) {
                System.out.println("You tied :/");
            }
            
            // If the user wants to play again
            System.out.println("Play again? ");
            String playAgainChoice = in.next();

            if (playAgainChoice.contains("y")) {
                playAgain = true;
            } else {
                playAgain = false;
                System.out.println("Bye.");
            }

        }
    }
}