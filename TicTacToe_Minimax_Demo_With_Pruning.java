import java.util.*;

public class TicTacToe_Minimax_Demo_With_Pruning
{

    public static int num_recursions = 0;
    public static void main(String[]args)
    {
        char[][] board = new char[3][3];

        init_board(board);

        boolean x_turn = true;
        
        while(!gameover(board))
        {
            print_board(board);
            
            if(x_turn) //player's turn
            {
                int[] move = get_move(board,x_turn);
                int row = move[0];
                int col = move[1];
                board[row][col] = 'X';
            }
            else //computer's turn
            {
                int[] move = get_minimax_move(board);
                int row = move[0];
                int col = move[1];
                board[row][col] = 'O';
            }
            
            x_turn = !x_turn;
            System.out.println("number of recursions: "+num_recursions);
            num_recursions = 0;

        }
        print_board(board);            

        if(tie(board))
        {
            System.out.println("It's a tie");
        }
        else if(x_win(board))
        {
            System.out.println("Congratulations player X, you win");
        }
        else
        {
            System.out.println("Congratulations player O, you win");
        }


    }

    //initializes the board to be empty
    public static void init_board(char[][] board)
    {
        for(int r=0;r<board.length;r++)
        {
            for(int c=0;c<board[r].length;c++)
            {
                board[r][c]='.';
            }
        }
    }

    //returns true if the game is over
    public static boolean gameover(char[][] board)
    {
        return x_win(board) || o_win(board) || tie(board);
    }

    //returns true if X wins
    public static boolean x_win(char[][] board)
    {
        return board[0][0]=='X'&&board[0][1]=='X'&&board[0][2]=='X' || 
        board[1][0]=='X'&&board[1][1]=='X'&&board[1][2]=='X' || 
        board[2][0]=='X'&&board[2][1]=='X'&&board[2][2]=='X' || 
        board[0][0]=='X'&&board[1][0]=='X'&&board[2][0]=='X' || 
        board[0][1]=='X'&&board[1][1]=='X'&&board[2][1]=='X' || 
        board[0][2]=='X'&&board[1][2]=='X'&&board[2][2]=='X' || 
        board[0][0]=='X'&&board[1][1]=='X'&&board[2][2]=='X' || 
        board[0][2]=='X'&&board[1][1]=='X'&&board[2][0]=='X';
    }

    //returns true if O wins
    public static boolean o_win(char[][] board)
    {
        return board[0][0]=='O'&&board[0][1]=='O'&&board[0][2]=='O' || 
        board[1][0]=='O'&&board[1][1]=='O'&&board[1][2]=='O' || 
        board[2][0]=='O'&&board[2][1]=='O'&&board[2][2]=='O' || 
        board[0][0]=='O'&&board[1][0]=='O'&&board[2][0]=='O' || 
        board[0][1]=='O'&&board[1][1]=='O'&&board[2][1]=='O' || 
        board[0][2]=='O'&&board[1][2]=='O'&&board[2][2]=='O' || 
        board[0][0]=='O'&&board[1][1]=='O'&&board[2][2]=='O' || 
        board[0][2]=='O'&&board[1][1]=='O'&&board[2][0]=='O';
    }

    //returns true if there is a tie
    public static boolean tie(char[][] board)
    {
        for(int r=0;r<board.length;r++)
        {
            for(int c=0;c<board[r].length;c++)
            {
                if(board[r][c]=='.')
                {
                    return false;
                }
            }
        }
        return true;
    }

    //returns the coordinates of the players move in the form of {row,col}
    //supress warnings to shut VSCode up about not closing the scanner
    @SuppressWarnings("resource")
    public static int[] get_move(char[][] board, boolean x_turn)
    {
        Scanner in = new Scanner(System.in);
        boolean valid_move = false;
        int row = -1;
        int col = -1;

        do
        {
            row = -1;
            col = -1;
            try
            {
                System.out.print("Enter a row and column for your move player ");
                if(x_turn)
                {
                    System.out.println("X");
                }
                else
                {
                    System.out.println("O");
                }
                String line = in.nextLine();
                String[] arr = line.split(" ");
                row = Integer.valueOf(arr[0]);
                col = Integer.valueOf(arr[1]);
            } catch(Exception e){}

            valid_move = is_valid_move(board,row,col);

        } while(!valid_move);

        int[] move = new int[2];
        move[0] = row;
        move[1] = col;

        return move;
    }


    //returns true if the specified move is a valid move
    public static boolean is_valid_move(char[][] board, int r, int c)
    {
        if(r<0||r>board.length-1||c<0||c>board[0].length-1||board[r][c]!='.')
        {
            System.out.println("invalid position");
            print_board(board);

            return false;
        }
        return true;
    }

    //prints the board
    public static void print_board(char[][] board)
    {
        System.out.println("\n\nboard:\n");
        for(int r=0;r<board.length;r++)
        {
            for(int c=0;c<board.length;c++)
            {
                System.out.print(board[r][c]);
            }
            System.out.println();
        }
        System.out.println("\n\n\n");
    }





    //runs minimax on every possible move
    //returns the best one (for the computer)
    //minimax driver code
    public static int[] get_minimax_move(char[][] board)
    {
        int best = -1000000;
        int[] best_move = new int[2];
        
        for(int r=0;r<board.length;r++)
        {
            for(int c=0;c<board.length;c++)
            {
                if(board[r][c]=='.')
                {
                    board[r][c] = 'O';
                    int move_score = minimax(board,false,0,-10000000,10000000);
                    board[r][c] = '.';

                    if(move_score > best)
                    {
                        best_move[0] = r;
                        best_move[1] = c;
                        best = move_score;
                    }
                }
            }
        }
        return best_move;
    }
    

    //returns the score for each board position based on the minimum or maximum score for the next possible moves
    public static int minimax(char[][] board, boolean max, int depth, int alpha, int beta)
    {
        num_recursions++;
        //end case
        //if the game is over, return the corresponding score based on who won
        if(gameover(board))
        {
            return evaluate(board,depth);
        }


        //if trying to maximize the score
        //loop through every possible move and run minimax trying to minimize the score
        //then return the highest value
        if(max)
        {
            int value = -10000000;

            for(int r=0;r<board.length;r++)
            {
                for(int c=0;c<board.length;c++)
                {
                    if(board[r][c]=='.')
                    {
                        board[r][c] = 'O';
                        int eval = minimax(board,false,depth+1,alpha,beta);
                        value = Math.max(value,eval);
                        board[r][c] = '.';

                        alpha = Math.max(alpha,eval);
                        if(beta <= alpha)
                        {
                            break;
                        }
                    }
                }
            }

            return value;
        }

        //if trying to minimize the score
        //loop through every possible move and run minimax trying to maximize the score
        //then return the lowest value
        else
        {
            int value = 10000000;

            for(int r=0;r<board.length;r++)
            {
                for(int c=0;c<board.length;c++)
                {
                    if(board[r][c]=='.')
                    {
                        board[r][c] = 'X';
                        int eval = minimax(board,true,depth+1,alpha,beta);
                        value = Math.min(value,eval);
                        board[r][c] = '.';

                        beta = Math.min(beta, eval);
                        if(beta <= alpha)
                        {
                            break;
                        }
                    }
                }
            }

            return value;
        }
    }

    //returns the value corresponding to who won the game
    public static int evaluate(char[][] board, int depth)
    {
        if(x_win(board))
        {
            return -100+depth;
        }
        else if(o_win(board))
        {
            return 100-depth;
        }
        return 0; 
    }
}   