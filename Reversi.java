package Reversi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by Kowshika Sarker on 12/8/2017.
 */

/*
System.out.println("\033[0m BLACK");
System.out.println("\033[31m RED");
System.out.println("\033[32m GREEN");
System.out.println("\033[33m YELLOW");
System.out.println("\033[34m BLUE");
System.out.println("\033[35m MAGENTA");
System.out.println("\033[36m CYAN");
System.out.println("\033[37m WHITE");
*/

class Coin
{
    int row;
    int column;

    public Coin(int row, int column) {
        this.row = row;
        this.column = column;
    }
}

class Node
{
    char[][] board;
    byte black;   //user
    byte white;   //ai
    Node()
    {
        board = new char[8][8];
        for(int i=0; i<8; i++)
        {
            Arrays.fill(board[i], '*');
        }
        black = 0;
        white = 0;
    }

    Node(Node node, int row, int col)
    {

    }

    void colorCoin(int row, int column, char ch)
    {
        board[row][column] = ch;
    }

    boolean boardFull()
    {
        return ((black + white) == 64);
    }
    void printBoard()
    {
        System.out.println("\u001B[1m\033[34m  A B C D E F G H");
        int i, j;
        char ch;
        for(i=0; i<8; i++)
        {
            System.out.print("\u001B[1m\033[35m" + (i+1));
            for(j=0; j<8; j++)
            {
                ch = board[i][j];
                if(ch == '*')
                {
                    System.out.print("\u001B[22m\033[37m *");
                }
                else if(ch == 'U')
                {
                    System.out.print("\u001B[1m\033[32m U");
                }
                else if(ch == 'A')
                {
                    System.out.print("\u001B[1m\033[31m A");
                }
                else if(ch == 'D')
                {
                    System.out.println("\u001B[1m\033[36m D");
                }
            }
            System.out.println();
        }
        System.out.println("\u001B[0m");
        System.out.println("User coins: " + black);
        System.out.println("AI coins : " + white);
    }

    void clear()
    {
        for(int i=0; i<8; i++)
        {
            Arrays.fill(board[i], '*');
        }
        black = 0;
        white = 0;
    }
}

class Tree
{
    Node node;
    ArrayList<Node> children;
    Tree(Node node)
    {
        this.node = node;
        children = new ArrayList<Node>();
    }

    Tree(Node node, ArrayList<Node> children)
    {
        this.node = node;
        this.children = children;
    }

    Tree()
    {
        node = null;
        children = new ArrayList<Node>();
    }

}

public class Reversi {
    Node current;
    Node validMoves;
    Reversi()
    {
        current = new Node();
        current.board[3][3] = 'A';
        current.board[3][4] = 'U';
        current.board[4][3] = 'U';
        current.board[4][4] = 'A';
        current.black = 2;
        current.white = 2;
        validMoves = new Node();
    }

    boolean checkDir(int r, int c, char p, int i, int j)
    {
        char o;
        if(p == 'U')
            o = 'A';
        else
            o = 'U';
        int oppCoin, x, y;

        if((i == 0) && (j==0))
            return false;

        oppCoin = 0;
        x = r + i;
        y = c + j;
        while ((x > -1) && (x < 8) && (y > -1) && (y < 8))
        {
            if(current.board[x][y] == '*')
                break;
            if(current.board[x][y] == o)
                oppCoin++;
            else
            {
                if(oppCoin == 0)
                    break;
                else
                    return true;
            }
            x += i;
            y += j;
        }
        return false;
    }

    boolean isValidMove(int r, int c, char p)
    {
        char o;
        if(p == 'U')
            o = 'A';
        else
            o = 'U';
        int oppCoin, i, j, x, y;

        for(i=-1; i<2; i++)
        {
            for(j=-1; j<2; j++)
            {
                if(checkDir(r, c, p, i, j))
                    return true;
            }
        }
        return false;
    }

    void calcValidMoves()
    {
        validMoves.clear();
        for(int i=0; i<8; i++)
        {
            for(int j=0; j<8; j++)
            {
                if(current.board[i][j] == '*')
                {
                    if(isValidMove(i, j, 'U'))
                    {
                        validMoves.board[i][j] = 'U';
                        validMoves.black++;
                    }
                    if(isValidMove(i, j, 'A'))
                    {
                        if(validMoves.board[i][j] == '*')
                            validMoves.board[i][j] = 'A';
                        else
                            validMoves.board[i][j] = 'D';
                        validMoves.white++;
                    }
                }
            }
        }
    }

    boolean userHasMoves()
    {
        //System.out.println("User valid moves = " + validMoves.black);
        return !(validMoves.black == 0);
    }

    boolean autoHasMoves()
    {
        //System.out.println("AI valid moves = " + validMoves.white);
        return !(validMoves.white == 0);
    }

    boolean gameOver()
    {
        if(current.boardFull())
            return true;
        //System.out.println("Board not full.");
        return !(userHasMoves() || autoHasMoves());
    }



    void updateBoard(int r, int c, char p)
    {
        char o;
        if(p == 'U')
            o = 'A';
        else
            o = 'U';
        int x, y, count = 0;

        for(int i=-1; i<2; i++)
        {
            for(int j=-1; j<2; j++)
            {
                if(checkDir(r, c, p, i, j))
                {
                    x = r + i;
                    y = c + j;
                    while ((x > -1) && (x < 8) && (y > -1) && (y < 8))
                    {
                        if(current.board[x][y] == o)
                        {
                            //System.out.println("Flipping at (" + x + "," + y + ")");
                            current.board[x][y] = p;
                            count++;
                        }
                        else
                            break;
                        x += i;
                        y += j;
                    }
                }
            }
        }
        current.board[r][c] = p;
        if(p == 'U')
        {
            current.black += count + 1;
            current.white -= count;
        }
        else
        {
            current.white += count + 1;
            current.black -= count;
        }
    }

    String userInput()
    {
        String input = "";
        char ch;
        Scanner scanner = new Scanner(System.in);
        while (true)
        {
            System.out.println("Enter a COLUMN no in between A snd H(inclusive)");
            ch = scanner.next().charAt(0);
            if((ch > '@') && (ch < 'I'))
            {
                break;
            }
            System.out.println("Invalid column no.");
        }
        input += ch;

        while (true)
        {
            System.out.println("Enter a ROW no in between 1 snd 8(inclusive)");
            ch = scanner.next().charAt(0);
            if((ch > '0') && (ch < '9'))
            {
                break;
            }
            System.out.println("Invalid row no.");
        }
        input += ch;
        return input;
    }

    void userTurn()
    {
        int r, c;
        String input;
        while (true)
        {
            input = userInput();
            System.out.println("User input = " + input);

            c = input.charAt(0) - 'A';
            r = input.charAt(1) - '1';
            System.out.println("row = " + r);
            System.out.println("column = " + c);
            if((validMoves.board[r][c] == 'U') || (validMoves.board[r][c] == 'D'))
                break;
            else
                System.out.println( input + " isn't a valid move. Please enter a valid position.");
        }
        System.out.println("State after user move.");
        updateBoard(r, c, 'U');
        current.printBoard();
        try {
            Thread.sleep(1600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void autoTurn()
    {
        Random random = new Random();
        int r, c;
        r = random.nextInt(8);
        c = random.nextInt(8);
        while ((validMoves.board[r][c] == '*') || (validMoves.board[r][c] == 'U'))
        {
            r = random.nextInt(8);
            c = random.nextInt(8);
        }
        String move = "";
        move += (char)('A' + c);
        move += (char)('1' + r);
        System.out.println("AI gives move at " + move);
        updateBoard(r, c, 'A');
        System.out.println("State after AI move.");
        current.printBoard();
    }

    void game()
    {
        System.out.println("Initial game state.");
        current.printBoard();
        while (true)
        {
            calcValidMoves();
            //validMoves.printBoard();
            if(gameOver())
            {
                System.out.println("** GAME OVER! **");
                System.out.println("User coins = " + current.black);
                System.out.println("AI coins = " + current.white);
                if(current.white > current.black)
                    System.out.println("AI Wins.");
                else if(current.white < current.black)
                    System.out.println("You win.");
                break;
            }
            if(userHasMoves())
            {
                System.out.println("Your turn.");
                userTurn();
            }
            else
            {
                System.out.println("You have no available moves.");
            }
            calcValidMoves();
            //validMoves.printBoard();
            if(gameOver())
            {
                System.out.println("** GAME OVER! **");
                System.out.println("User coins = " + current.black);
                System.out.println("AI coins = " + current.white);
                if(current.white > current.black)
                    System.out.println("AI Wins.");
                else if(current.white < current.black)
                    System.out.println("You win.");
                break;
            }
            if(autoHasMoves())
            {
                System.out.println("AI's turn.");
                autoTurn();

            }
            else
            {
                System.out.println("AI has no available moves.");
            }
        }
    }

    public static void main(String[] args)
    {
        Reversi reversi = new Reversi();
        reversi.game();
    }

}
