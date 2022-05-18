
import twitter4j.*;
import java.util.List;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Date;

public class TwitterJ {
    private Twitter twitter;
    private PrintStream consolePrint;
    private List<Status> statuses;
    private List<String> terms;
    private String popularWord;
    private int frequencyMax;

    public TwitterJ(PrintStream console)
    {
        // Makes an instance of Twitter - this is re-useable and thread safe.
        // Connects to Twitter and performs authorizations.
        twitter = TwitterFactory.getSingleton();
        consolePrint = console;
        statuses = new ArrayList<>();
        terms = new ArrayList<>();
    }

    /*  Part 1 */
    /*
     * This method tweets a given message.
     * @param String  a message you wish to Tweet out
     */
    public void tweetOut(String message) throws TwitterException, IOException
    {
        twitter.updateStatus(message);
    }


    /*  Part 2 */
    /*
     * This method queries the tweets of a particular user's handle.
     * @param String  the Twitter handle (username) without the @sign
     */
    @SuppressWarnings("unchecked")
    public void queryHandle(String handle) throws TwitterException, IOException
    {
        statuses.clear();
        terms.clear();
        fetchTweets(handle);
        splitIntoWords();
        removeCommonEnglishWords();
        sortAndRemoveEmpties();
    }

    /*
     * This method fetches the most recent 2,000 tweets of a particular user's handle and
     * stores them in an arrayList of Status objects.  Populates statuses.
     * @param String  the Twitter handle (username) without the @sign
     */
    public void fetchTweets(String handle) throws TwitterException, IOException
    {
        // Creates file for dedebugging purposes
        PrintStream fileout = new PrintStream(new FileOutputStream("tweets.txt"));
        Paging page = new Paging (1,200);
        int p = 1;
        while (p <= 10)
        {
            page.setPage(p);
            statuses.addAll(twitter.getUserTimeline(handle,page));
            p++;
        }
        int numberTweets = statuses.size();
        fileout.println("Number of tweets = " + numberTweets);

        int count=1;
        for (Status j: statuses)
        {
            fileout.println(count+".  "+j.getText());
            count++;
        }
    }

    /*
     * This method takes each status and splits them into individual words.
     * Remove punctuation by calling removePunctuation, then store the word in terms.
     */
    public void splitIntoWords()
    {

    }

    /*
     * This method removes common punctuation from each individual word.
     * Consider reusing code you wrote for a previous lab.
     * Consider if you want to remove the # or @ from your words. Could be interesting to keep (or remove).
     * @ param String  the word you wish to remove punctuation from
     * @ return String the word without any punctuation
     */
    private String removePunctuation( String s )
    {
        return null;

    }

    /*
     * This method removes common English words from the list of terms.
     * Remove all words found in commonWords.txt  from the argument list.
     * The count will not be given in commonWords.txt. You must count the number of words in this method.
     * This method should NOT throw an exception.  Use try/catch.
     */
    @SuppressWarnings("unchecked")
    private void removeCommonEnglishWords()
    {


    }

    /*
     * This method sorts the words in terms in alphabetically (and lexicographic) order.
     * You should use your sorting code you wrote earlier this year.
     * Remove all empty strings while you are at it.
     */
    @SuppressWarnings("unchecked")
    public void sortAndRemoveEmpties()
    {


    }

    /*
     * This method returns the most common word from terms.
     * Consider case - should it be case sensitive?  The choice is yours.
     * @return String the word that appears the most times
     * @post will populate the frequencyMax variable with the frequency of the most common word
     */
    @SuppressWarnings("unchecked")
    public String mostPopularWord()
    {
        return null;
    }

    /*
     * This method returns the number of times the most common word appears.
     * Note:  variable is populated in mostPopularWord()
     * @return int frequency of most common word
     */
    public int getFrequencyMax()
    {
        return 0;
    }


    /*  Part 3 */
    public void investigate ()
    {
        //Enter your code here
    }

    /*
     * This method determines how many people near Churchill Downs
     * tweet about the Kentucky Derby.
     */
    public void sampleInvestigate ()
    {
        Query query = new Query("Kentucky Derby");
        query.setCount(100);
        query.setGeoCode(new GeoLocation(38.2018,-85.7687), 5, Query.MILES);
        query.setSince("2021-5-1");
        try {
            QueryResult result = twitter.search(query);
            System.out.println("Count : " + result.getTweets().size()) ;
            for (Status tweet : result.getTweets()) {
                System.out.println("@"+tweet.getUser().getName()+ ": " + tweet.getText());
            }
        }
        catch (TwitterException e) {
            e.printStackTrace();
        }
        System.out.println();
    }
}
