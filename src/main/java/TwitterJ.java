
import org.javacord.api.*;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import twitter4j.*;

import java.awt.*;
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
        for (int i = 0; i < statuses.size(); i++) { // loop through statuses
            String[] split = statuses.get(i).getText().split(" ");
            for (int j = 0; j < split.length; j++) { // through individual words in statuses
                String word = split[j];
                word = removePunctuation(split[j]); // need remove punctuation here, as not called in runner.
                terms.add(word);
            }// end of split for-loop
        }// end of statuses for-loop
    }// end of splitIntoWords method

    /*
     * This method removes common punctuation from each individual word.
     * Consider reusing code you wrote for a previous lab.
     * Consider if you want to remove the # or @ from your words. Could be interesting to keep (or remove).
     * @ param String  the word you wish to remove punctuation from
     * @ return String the word without any punctuation
     */
    private String removePunctuation( String s )
    {
        s = s.replaceAll("[^A-Za-z0-9]", ""); // regex removes all elements that are not alphanumeric characters
        return s;
    }// end of removePunctuation method

    /*
     * This method removes common English words from the list of terms.
     * Remove all words found in commonWords.txt  from the argument list.
     * The count will not be given in commonWords.txt. You must count the number of words in this method.
     * This method should NOT throw an exception.  Use try/catch.
     */
    @SuppressWarnings("unchecked")
    private void removeCommonEnglishWords() {
        try {
            File file = new File("commonWords.txt");
            Scanner fromFile = new Scanner(file);
            while (fromFile.hasNextLine()) { // while the next line is not empty
                String currentCommonWord = fromFile.nextLine();
                for (int i = 0; i < terms.size(); i++) { // loop through terms
                    if (terms.get(i).equalsIgnoreCase(currentCommonWord)) {
                        terms.remove(i);
                        i--;
                    }// remove term if common word
                }// end of terms for-loop
            }// end of while-loop - finished reading text file
        }catch (FileNotFoundException e) {e.printStackTrace();}
    }// end of removeCommonEnglishWords method

    /*
     * This method sorts the words in terms in alphabetically (and lexicographic) order.
     * You should use your sorting code you wrote earlier this year.
     * Remove all empty strings while you are at it.
     */
    @SuppressWarnings("unchecked")
    public void sortAndRemoveEmpties()
    {
        for(int i = 0; i < terms.size()-1 ; i++) { // selection sort through terms
            int minValue = i;
            for(int j = i + 1; j < terms.size(); j++) { // loop through terms
                if (terms.get(j).compareToIgnoreCase(terms.get(minValue)) < 0) minValue = j;
            }// end of terms for-loop
            String temp = terms.get(i);
            terms.set(i, terms.get(minValue));
            terms.set(minValue, temp);
        }// end of selection sort
        for(int i = 0; i < terms.size()-1 ; i++) { // loop through terms
            if(terms.get(i).equals("") || terms.get(i).equals(" ")){
                terms.remove(i);
                i--;
            }// remove if term is empty
        }// end of terms for-loop
    }// end of sortAndRemoveEmpties method

    /*
     * This method returns the most common word from terms.
     * Consider case - should it be case sensitive?  The choice is yours.
     * @return String the word that appears the most times
     * @post will populate the frequencyMax variable with the frequency of the most common word
     */
    @SuppressWarnings("unchecked")
    public String mostPopularWord()
    {
        ArrayList<String> words = new ArrayList<>();
        ArrayList<Integer> frequency = new ArrayList<Integer>();
        for(int i = 0; i < terms.size(); i++) { // loop through terms
            while(!words.contains(terms.get(i).toLowerCase())) { // check if words list contains terms from terms list (caps are ignored)
                words.add(terms.get(i).toLowerCase());
            }// end of conditional while
        }// end of terms for loop - should result in no duplicates
        for (int i = 0; i < words.size(); i++) { // loop through words
            frequency.add(0); // avoids index ut of bounds
            for (int j = 0; j < terms.size(); j++) { // loop through terms
                if(words.get(i).equalsIgnoreCase(terms.get(j))){
                    frequency.set(i, frequency.get(i) + 1);
                }// adds 1 if word in list
            }// end of terms for-loop
        }// end of words for-loop
        int max = -1; // max set to impossible number
        int maxIndex = 0;
        for(int i = 0; i < frequency.size(); i++) { // loop through frequency
            if(frequency.get(i) > max){
                max = frequency.get(i);
                maxIndex = i;
            }// returns maxIndex
        }// end of frequency for-loop
        popularWord = words.get(maxIndex);// gets word at max index
        return popularWord;
    }// end of mostPopularWord method

    /*
     * This method returns the number of times the most common word appears.
     * Note:  variable is populated in mostPopularWord()
     * @return int frequency of most common word
     */
    public int getFrequencyMax()
    {
        ArrayList<String> words = new ArrayList<>();
        ArrayList<Integer> frequency = new ArrayList<Integer>();
        for(int i = 0; i < terms.size(); i++) { // loop through terms
            while(!words.contains(terms.get(i).toLowerCase())) { // check if words list contains terms from terms list (caps are ignored)
                words.add(terms.get(i).toLowerCase());
            }// end of conditional while
        }// end of terms for loop - should result in no duplicates
        for (int i = 0; i < words.size(); i++) { // loop through words
            frequency.add(0); // avoids index ut of bounds
            for (int j = 0; j < terms.size(); j++) { // loop through terms
                if(words.get(i).equalsIgnoreCase(terms.get(j))){
                    frequency.set(i, frequency.get(i) + 1);
                }// adds 1 if word in list
            }// end of terms for-loop
        }// end of words for-loop
        int max = -1; // max set to impossible number
        int maxIndex = 0;
        for(int i = 0; i < frequency.size(); i++) { // loop through frequency
            if(frequency.get(i) > max){
                max = frequency.get(i);
                maxIndex = i;
            }// returns maxIndex
        }// end of frequency for-loop
        frequencyMax = frequency.get(maxIndex);
        return frequencyMax;
    }// end of getFrequencyMax method

    /*  Part 3 */
    public void investigate ()
    {
        DiscordApi api = new DiscordApiBuilder().setToken("OTc1NTI4NDI5NjE1MjAyNDA0.GpnlEk.CYmO4QJZ6ZdqAHdFCM4mjG8T02DzaDPD0jvEXY").login().join();
        api.addMessageCreateListener(follow -> {
            String userMessage = follow.getMessageContent();
            String usersString = "";
            if (userMessage.substring(0,7).equals("!follow")) {
                usersString = userMessage.substring(7);
            }
            usersString = usersString.replaceAll("[^A-Za-z0-9_,]", "");
            String[] users = usersString.split("[,]");
            ArrayList<String> cleanList = new ArrayList<>();
            for (int i = 0; i < users.length; i++) {
                if(users[i].length() > 4 && users[i].length() < 15) {
                    cleanList.add(users[i]);
                }
            }
            try {
                FileWriter fileWriter = new FileWriter("followersList.txt");
                BufferedWriter writer = new BufferedWriter(fileWriter);
                for(int i = 0; i < cleanList.size(); i++) {
                    writer.write(users[i]);
                    if(i != users.length-1) {
                        writer.write("\n");
                    }
                    writer.flush();
                }
                follow.getChannel().sendMessage("DONE!");
            } catch (IOException e) {
                e.printStackTrace();
            }// try catch for writing into file
        });
        api.addMessageCreateListener(listFollowers -> {
            String userMessage = listFollowers.getMessageContent();
            String usersString = "";
            if (userMessage.equals("!showf")) {
                try {
                    File file = new File("followersList.txt");
                    Scanner fromFile = new Scanner(file);
                    String output = "";
                    while (fromFile.hasNextLine()) { // while the next line is not empty
                        String currentUser = fromFile.nextLine();
                        output += currentUser;
                        if(fromFile.hasNextLine()) {
                            output += ", ";
                        }
                    }// end of while-loop - finished reading text file
                    listFollowers.getChannel().sendMessage(output);
                }catch (FileNotFoundException e) {e.printStackTrace();}
                ArrayList<String> users = new ArrayList<>();
                ArrayList<Status> statusList = new ArrayList<>();
                try {
                    File file = new File("followersList.txt");
                    Scanner fromFile = new Scanner(file);
                    while (fromFile.hasNextLine()) { // while the next line is not empty
                        users.add(fromFile.nextLine());
                    }// end of while-loop - finished reading text file
                    for (int i = 0; i < users.size(); i++) {
                        Status status = twitter.showStatus(Long.parseLong(users.get(i)));
                        statusList.add(status);
                    }
                    for (int i = 0; i < statusList.size(); i++) {
                        System.out.println("hello");
                        System.out.println(statusList.get(i).getUser().getScreenName() + " said: " + statusList.get(i).getText());
                        listFollowers.getChannel().sendMessage(statusList.get(i).getUser().getScreenName() + " said: " + statusList.get(i).getText());
//                        new MessageBuilder().setEmbed(new EmbedBuilder()
//                                .setTitle(statusList.get(i).getUser().getScreenName() + " said: ")
//                                .setDescription(statusList.get(i).getText())
//                                .setColor(Color.ORANGE))
//                                .send(showLatestTweets.getChannel());;
                    }
                }catch (FileNotFoundException | TwitterException e) {e.printStackTrace();}
            }

        });
        api.addMessageCreateListener(showLatestTweets -> {
            if(showLatestTweets.getMessageContent().equals("!showtwt")) {
                /*ArrayList<String> users = new ArrayList<>();
                ArrayList<Status> statusList = new ArrayList<>();
                try {
                    File file = new File("followersList.txt");
                    Scanner fromFile = new Scanner(file);
                    while (fromFile.hasNextLine()) { // while the next line is not empty
                        users.add(fromFile.nextLine());                        
                    }// end of while-loop - finished reading text file
                    for (int i = 0; i < users.size(); i++) {
                        Status status = twitter.showStatus(Long.parseLong(users.get(i)));
                        statusList.add(status);
                    }
                    for (int i = 0; i < statusList.size(); i++) {
                        System.out.println(statusList.get(i).getUser().getScreenName() + " said: " + statusList.get(i).getText());
                        showLatestTweets.getChannel().sendMessage(statusList.get(i).getUser().getScreenName() + " said: " + statusList.get(i).getText());
//                        new MessageBuilder().setEmbed(new EmbedBuilder()
//                                .setTitle(statusList.get(i).getUser().getScreenName() + " said: ")
//                                .setDescription(statusList.get(i).getText())
//                                .setColor(Color.ORANGE))
//                                .send(showLatestTweets.getChannel());;
                    }
                }catch (FileNotFoundException | TwitterException e) {e.printStackTrace();}*/
            }
        });

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
}// end of TwitterJ class