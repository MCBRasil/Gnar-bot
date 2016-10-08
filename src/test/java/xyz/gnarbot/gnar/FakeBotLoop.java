package xyz.gnarbot.gnar;

import xyz.gnarbot.gnar.fakes.FakeBot;

import java.util.Scanner;

public class FakeBotLoop
{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        
        boolean running = true;
        
        while (running)
        {
            System.out.print("Query | ");
            
            String query = sc.nextLine();
            
            switch (query)
            {
                case "-stop":
                    running = false;
                    break;
            }
            
            FakeBot.INSTANCE.send(query);
        }
    }
}
