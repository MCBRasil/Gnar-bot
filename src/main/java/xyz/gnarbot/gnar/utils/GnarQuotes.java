package xyz.gnarbot.gnar.utils;


import java.util.Random;

//TODO: Put this into a file
public class GnarQuotes
{
    private static final Random random = new Random();
    
    private static final String[] quotes =
            {
                    "Gnar gada!",
                    "Gnar!",
                    "Shubbanuffa.",
                    "Vimaga.",
                    "Nakotak.",
                    "Kshaa!",
                    "Vigishu!",
                    "Wap!",
                    "Hwa!",
                    "Vrooboo.",
                    "Raag!",
                    "Wabbo!",
                    "Gnar squeals.",
                    "Mega Gnar roars.",
                    "Gnar.",
                    "Maga.",
                    "Shagdovala!",
                    "Hursh, rao!",
                    "Ovagarava!",
                    "Onna legga.",
                    "Okalannomaka.",
                    "Ahanga!",
                    "Oga lagga.",
                    "Goova.",
                    "Oga manni maxa.",
                    "Reeshoova!",
                    "Fue huega.",
                    "Okanoo.",
                    "Ganaloo mo.",
                    "Gnar groans.",
                    "Gnar yelps.",
                    "Gnar scoffs.",
                    "Gnar sniffs.",
                    "Mega Gnar snarls.",
                    "Shoo shoo? Bahnah!",
                    "Mega Gnar begins to roar, but chokes and coughs.",
                    "Gnar chants.",
                    "Mega Gnar chants.",
                    "Shugi shugi shugi!",
                    "Haygo vaygo!",
                    "Jay Watford.",
                    "Tibbahs!",
                    "Shargh!",
                    "Demaglio!",
                    "Marmess!",
                    "Mo'kay.",
                    "Mega Gnar roars.",
                    "Gnar laughs.",
                    "Mega Gnar chuckles.",
                    "GNAR!"
            };
    
    public static String getRandomQuote()
    {
        int randomNumber = random.nextInt(quotes.length);
        
        return quotes[randomNumber];
    }
    
}
