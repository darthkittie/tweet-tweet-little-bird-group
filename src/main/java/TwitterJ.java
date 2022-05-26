import org.javacord.api.*;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import twitter4j.*;
import java.awt.*;
import java.util.List;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class TwitterJ {
    private Twitter twitter;
    private PrintStream consolePrint;
    private List<Status> statuses;
    private List<String> terms;
    private String popularWord;
    private int frequencyMax;
    private final DiscordApi api = new DiscordApiBuilder().setToken("OTc4NzcxMTU4NTk1OTYwODMy.GYbDpf.e5hdiR0sBvef08x7_frcwRVaN_0dFZiYV_O-K4").login().join();


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
        // Creates file for debugging purposes
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
        for (int i = 0; i < statuses.size(); i++) {
            String[] split = statuses.get(i).getText().split(" ");

            for (int j = 0; j < split.length; j++) {
                String word = removePunctuation(split[j]); // need remove punctuation here, as not called in runner.
                terms.add(word);
            }

        }
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
        s = s.replaceAll("[^A-Za-z0-9]", ""); // regex to remove all elements that are not alphanumeric characters
        // "^" indicates not
        // A-Z checks all letters between A-Z; same with a-z and 0-9
        // regex refers to characters that form a pattern to be searched through
        // When using ^ and our characters, we tell the regex to search for all characters that are not our characters
        // The brackets indicate to search through each letter individually rather than with a pattern.
        //  -> "[!@]" searches through ! and @ separately while "!@" searches for patterns of "!@" (contains)
        // Replace through searches for the regex and replaces it with the string provided ("")
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

            while (fromFile.hasNextLine()) {
                String currentCommonWord = fromFile.nextLine();

                for (int i = 0; i < terms.size(); i++) {
                    if (terms.get(i).equalsIgnoreCase(currentCommonWord)) {
                        terms.remove(i);
                        i--;
                    }// remove term if common word
                }

            }// finished reading text file

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
            int minIndex = i;

            for(int j = i + 1; j < terms.size(); j++) { // loop through terms
                if (terms.get(j).compareToIgnoreCase(terms.get(minIndex)) < 0) minIndex = j;
            }

            String temp = terms.get(i); // needed to swap location
            terms.set(i, terms.get(minIndex));
            terms.set(minIndex, temp); // moves to proper index
        }// end of selection sort

        for(int i = 0; i < terms.size()-1 ; i++) {
            if(terms.get(i).equals("") || terms.get(i).equals(" ")){
                terms.remove(i);
                i--;
            }// remove if term is empty
        }

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
        ArrayList<Integer> frequency = new ArrayList<>();

        for(int i = 0; i < terms.size(); i++) {
            while(!words.contains(terms.get(i).toLowerCase())) { // check if words list contains terms from terms list (caps are ignored)
                words.add(terms.get(i).toLowerCase());
            }// end of conditional while
        }// should result in no duplicates

        for (int i = 0; i < words.size(); i++) {
            frequency.add(0); // avoids index out of bounds

            for (int j = 0; j < terms.size(); j++) {
                if(words.get(i).equalsIgnoreCase(terms.get(j))){
                    frequency.set(i, frequency.get(i) + 1);
                }// adds 1 if word in list
            }

        }

        int max = -1; // max set to impossible number
        int maxIndex = 0;

        for(int i = 0; i < frequency.size(); i++) {
            if(frequency.get(i) > max){
                max = frequency.get(i);
                maxIndex = i;
            }// returns maxIndex
        }

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
        ArrayList<Integer> frequency = new ArrayList<>();

        for(int i = 0; i < terms.size(); i++) {
            while(!words.contains(terms.get(i).toLowerCase())) { // check if words list contains terms from terms list (caps are ignored)
                words.add(terms.get(i).toLowerCase());
            }// end of conditional while
        }// should result in no duplicates

        for (int i = 0; i < words.size(); i++) {
            frequency.add(0); // avoids index ut of bounds

            for (int j = 0; j < terms.size(); j++) {
                if(words.get(i).equalsIgnoreCase(terms.get(j))){
                    frequency.set(i, frequency.get(i) + 1);
                }// adds 1 if word in list
            }

        }

        int max = -1; // max set to impossible number
        int maxIndex = 0;

        for(int i = 0; i < frequency.size(); i++) {
            if(frequency.get(i) > max){
                max = frequency.get(i);
                maxIndex = i;
            }// returns maxIndex
        }

        frequencyMax = frequency.get(maxIndex);
        return frequencyMax;
    }// end of getFrequencyMax method

    /*  Part 3 */
    public void investigate ()
    {
        twitterDiscordFollow();
        twitterDiscordShow();
        twitterDiscordRemove();
    }

    public void twitterDiscordFollow() // adds users to a file
    {
        api.addMessageCreateListener(follow -> { // lambda expression (from javacord docs) that checks messages in discord channel
            String userMessage = follow.getMessageContent();
            String usersString = "";

            if (userMessage.substring(0,7).equals("!follow")) { // checks if user's message starts with "!follow"
                usersString = userMessage.substring(7); // grabs everything that follows
            }

            usersString = usersString.replaceAll("[^A-Za-z0-9_,]", ""); // removes all illegal characters and commas (to split)
            String[] users = usersString.split(",");
            ArrayList<String> cleanList = new ArrayList<>();

            for (int i = 0; i < users.length; i++) {
                if(users[i].length() > 0 && users[i].length() < 15) {
                    cleanList.add(users[i]);
                }// adds to new list if username length is correct
            }

            try {
                FileWriter fileWriter = new FileWriter("followersList.txt",true); // append means that file won't be cleared
                BufferedWriter writer = new BufferedWriter(fileWriter);

                for(int i = 0; i < cleanList.size(); i++) {
                    File file = new File("followersList.txt");
                    Scanner fromFile = new Scanner(file);
                    boolean contains = false;

                    while (fromFile.hasNextLine()) {
                        if(cleanList.get(i).equals(fromFile.nextLine())) {
                            contains = true;
                        }
                    }// used with if-else that follows to make sure there are no duplicates

                    if(contains) {
                        follow.getChannel().sendMessage("Could not add " + cleanList.get(i) + "! \nThey are already in the list.");
                    }else {
                        writer.write(cleanList.get(i) + "\n");
                        follow.getChannel().sendMessage("Added " + cleanList.get(i) + "!");
                    }// checks if name is already in list
                    writer.flush();
                }// end of clean list loop

            } catch (IOException e) {e.printStackTrace();}
        });
    }// end of discord follow method

    public void twitterDiscordShow()
    {
        api.addMessageCreateListener(showFollowing -> {
            String userMessage = showFollowing.getMessageContent();

            if (userMessage.equals("!show")) { // checks if user inputs "!show"

                try {
                    File file = new File("followersList.txt");
                    Scanner fromFile = new Scanner(file);
                    String output = "";

                    while (fromFile.hasNextLine()) {
                        String currentUser = fromFile.nextLine();
                        output += currentUser;

                        if(fromFile.hasNextLine()) {
                            output += ", ";
                        } // Use to create progress statement

                    }// end of while-loop - finished reading text file

                    if(!(output.equals("") || output.equals(" "))) {
                        showFollowing.getChannel().sendMessage("Showing latest tweets/replies from " + output + "!"); // using listener to send a message in channel
                    }else {
                        showFollowing.getChannel().sendMessage("You are not following anyone yet :(");
                    }// only start showing messages if output is not empty
                }catch (FileNotFoundException e) {e.printStackTrace();}

                ArrayList<String> users = new ArrayList<>();
                ArrayList<Status> statusList = new ArrayList<>();

                try {
                    File file = new File("followersList.txt");
                    Scanner fromFile = new Scanner(file);

                    while (fromFile.hasNextLine()) {
                        users.add(fromFile.nextLine());
                    }// finished reading text file

                    for (int i = 0; i < users.size(); i++) {
                        // this uses the file writing from part 2 - instead of pulling 200 pages, just 1
                        Paging page = new Paging (1,1);
                        page.setPage(1);
                        statusList.addAll(twitter.getUserTimeline(users.get(i),page));
                    }

                    for (int i = 0; i < statusList.size(); i++) {
                        /*---------------------------------------------------------------------------------------*/

                        // print out the tweets using the message builder class of javacord - using documentation
                        new MessageBuilder()
                                .setEmbed(new EmbedBuilder()
                                        .setTitle(statusList.get(i).getUser().getScreenName() + " said: ")
                                        .setDescription(statusList.get(i).getText())
                                        .setColor(Color.BLUE))
                                .send(showFollowing.getChannel());
                        /*---------------------------------------------------------------------------------------*/
                    }

                }catch (FileNotFoundException | TwitterException e) {e.printStackTrace();}
            }

        });
    }// end of discord show method

    public void twitterDiscordRemove()
    {
        api.addMessageCreateListener(remove -> {
            String userMessage = remove.getMessageContent();
            String usersString = "";

            if (userMessage.substring(0,7).equals("!remove")) {
                usersString = userMessage.substring(7);
            } // checks if user input begins with "!remove"

            usersString = usersString.replaceAll("[^A-Za-z0-9_,]", ""); // removes all illegal characters and commas (to split)
            String[] removeUsers = usersString.split(",");
            ArrayList<String> cleanList = new ArrayList<>();

            for (int i = 0; i < removeUsers.length; i++) {
                if(removeUsers[i].length() > 0 && removeUsers[i].length() < 15) {
                    cleanList.add(removeUsers[i]);
                }
            }// creates new array of users

            try {

                ArrayList<String> users = new ArrayList<>();
                File file = new File("followersList.txt");
                Scanner fromFile = new Scanner(file);

                while (fromFile.hasNextLine()) { // while the next line is not empty
                    users.add(fromFile.nextLine());
                }// end of while-loop - finished reading text file

                for (int i = 0; i < cleanList.size(); i++) {
                    for (int j = 0; j < users.size(); j++) {
                        if (users.get(j).equals(cleanList.get(i))) {
                            users.remove(j);
                            j--;
                            remove.getChannel().sendMessage("Successfully removed " + cleanList.get(i) + "!");

                        }// remove if there is a match
                    }
                }

                FileWriter fileWriter = new FileWriter("followersList.txt"); // since no append - should result in brand-new file
                BufferedWriter writer = new BufferedWriter(fileWriter);

                for(int i = 0; i < users.size(); i++) {
                    writer.write(users.get(i) + "\n"); // write each into file & include "\n"
                    writer.flush();
                }

            } catch (IOException e) {e.printStackTrace();}// try catch for writing into file
        });
    }// end of discord remove method

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