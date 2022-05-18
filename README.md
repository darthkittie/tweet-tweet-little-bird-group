# Epic Quest (Co-op?): Tweet Tweet Little Birdie
**`Quest giver: Governor Nighthill`**
>I have read about all your accolades, very impressive.  I knew you would be going far from when you first helped me. Now, I need to call on you for one last time.  There is even newer technology where you send digital talking birds to other who can then respond.

## Objectives required to complete
This lab has us working with an API that allows us to interact with Twitter.  We have worked with other APIs to date, the Java API being the most common.

Remember an API is an application programming interface.  It specifies how software components interact.  APIs allows communication to happen between different applications as sending and receiving information happens dynamically.

We will be interacting with Twitter by writing and reading tweets to the platform.

There may be days that parts of the larger project are due before the entire project is due, missing these deliverable deadlines will result in a loss of points on those assignments and on the overall project.

### Objective 1 - Designing a Plan
**A design plan for this project is required to be submitted and approved before coding work can begin.  Make sure you receive approval first, beginning without an approved design plan can result in a loss of points.  This plan will need to be a full design plan look at older repos to get an idea on how a full design plan should be made it will need to be more detailed than normal**

Our design plan should outline the steps being accomplished in each method of our program.  The following sections will be examples of what is expected from us when it comes to the design plan.

```
main method:
    Step 1: List the major tasks (limit to no more than four or five tasks)
        if you have more than four or five tasks that is a good indicator that you should break out into another method
    Step 2: Decompose the design of each major step in the Step 1 design
    Step 3: Decompose remaining sections of the design in Step 2 to small enough section so they can be easily coded
        this might not be possible for ALL sections of part 2, but most math things or boolean expressions could be written
    Step 4: Combine design components from Steps 1, 2, 3, and 4
method X: (this would be an actual method name in the code)
    repeat the 4 steps from above
method Y: (this again would be an actual method name in the code)
    repeat the 4 steps from above
```

All the steps above are combined into one plan.  Step 4 means that you are combining all the other steps into a single plan.

### Objective 2 - Cloning
- Clone this project into IntelliJ
    - remember to place it into a correct folder

### Objective 3 - Branching from the main
- Click on the word "Git" in the bottom left corner of IntelliJ
- Select the "main" under the "Local"
    - right-click on main and select "+ New Branch"
- On the pop up name the branch `dev`
    - Leave the checkmark in "Checkout Branch"
    - Click "Create"

## Quest Requirements
The bullet points in this section are the bare minimum needed. There are often times when exact details are missing as it would give away the answer. Therefore, we should realize that some more work and logic is required with certain steps.

We will use Twitter4j API to do our connecting and twitter interaction.  You can search for `Twitter4j` and find the JavaDoc for this API to see what else you could develop.

### Objective 1 - Twitter4j.properties
This file hold the credentials to access Twitter data.  Twitter requires permission to use their API.

The values in this file are default values.  They grant read-only access to Twitter.  This is enough to complete the assignment.

However, it does not allow multiple students to use the credentials at the same time.  Therefore, you could encounter run-time errors when there are more than one student trying to access the credentials (run their version of the program).  To avoid this error you should consider making (or using) your own Twitter account.

### (Optional)
The next 5 objectives are optional and should be followed if you are going to make your own twitter account for this project and not use the read only keys provided.

#### Objective 1 - Creating a Twitter Account
Making a Twitter account requires a valid email address and a valid mobile number (to enable a developer account).

Using a fake name is acceptable.  Deleting the entire Twitter account is possible once the assignment is done.

#### Objective 2 - Creating a Dev Account
We need to enable dev account on our Twitter.

1. Visit [Apps Page](https://apps.twitter.com)
2. Select `Create New App`
    - we may need to log in
    - we may need to apply for a dev account
        - we should indicate that we are using this for education and we are "students"

#### Objective 3 - Getting the Keys
Once we have gone through the dev account setup we are ready to make our project

1. Create a New project on the app page
2. Give it a name and other info
3. We will receive a popup with our consumer keys
    - COPY these keys into a document
4. Make sure the App Permissions are `Read and Write`
5. Generate new Authentication Tokens
    - COPY these keys into a document

#### Object 4 - twitter4j.properties
**ONLY DO THIS IS YOU HAVE YOU OWN TWITTER ACCOUNT YOU ARE WORKING WITH**

1. Copy the Keys and Secrets into the twitter4j.properties file overwriting the default values in there
    - the consumer key and secret you are given right you make the project through the developer website
    - the access token and secret you will need to generate on the keys and token page of the project
        - make sure the project have read and write access to the account you are using
        - if you don't want the program to be able to tweet for you leave the permission as read only

If you forget to copy these token before close their windows you can always regenerate them.

#### Object 5 - Testing the App
In the `TwitterRunner` class we can replace the `String message` with our own message to tweet out on the account.  If you replace this message and run our code, it will send the message to your twitter account.

**End of optional part**

### Objective 2 - Most Common Word
We will access the 2,000 most recent tweets of a person's handle.  We want to determine the most common word they use.

The procedure for doing this is:
- get the most recent tweets (store into a data structure)
- split the tweets into individual words (by blanks)
- remove extraneous punctuation characters
- remove common and uninteresting words (the, a, and)
- find the word that occurs most often in the data structure

The following methods will need implementation in the `TwitterJ` class

```Java
void splitIntoWords()
```
- this method takes all the tweets in `statuses` and splits them up into individual words storing them in `terms`

```Java
String removePunctuation(String tweet)()
```
- choose what specific punctuation to remove.  as we run the program, we may decide to remove different punctuation symbols.  we might find that sometimes people use extra spacing in their tweets and we may want to remove those too.

```Java
void removeCommonEnglishWords()
```
- the list of common words can be found in `commonWords.txt` use this list to compare against terms and remove any terms that appear in the file.  we can update these words by modifying this file.

```Java
void sortAndRemoveEmpties()
```
- think about how you could implement a way to sort words, we have talked about some different sorts this year and seen their code.  you cannot use any prebuilt in java sorting algorithms.  you can only use code you have written yourself.

```Java
String mostPopularWord()
```
- there are multiple ways to implement this algorithm.  the most efficient way would is to find the word that is in the largest consecutive group of equal words.  then you can populate the frequencyMax variable with the times of times the most common word occurred.

```Java
int getWordFrequency()
```
- getter to report the number of occurrences the most common word appears

Below is an example of how the program could run, the result we get from these exact handles could be different as some time has passed since these handles were run
```
Please enter a Twitter handle, do not include the @ symbol --> taylorswift13
The most common word from @taylorswift13 is: love.
The word appears 149 times.

Please enter a Twitter handle, do not include the @ symbol --> billnye
The most common word from @billnye is: science.
The word appears 220 times.

Enter Twitter handle (without the @ symbol, type done to quit) --> done
```

### Objective 3 - Going Beyond
For this section you will need to brainstorm something interesting to do with Twitter.

We have access to useful information
- [JavaDoc](https://twitter4j.org/oldjavadocs/4.0.4/index.html)

- [Code examples](https://twitter4j.org/en/code-examples.html)

Some examples of ideas could be
1. Filter your home timeline (these are all the tweets from people you follow) to avoid a particular term.
2. Count the number of tweets originating from your zip code that contain the word "prom" or any other word you are interested in.
3. Search for tweets:  (a) created in the last week, (b) that contained the term "giraffes", and (c) were sent from a location within a 10 mile radius of East.   Some sample code for how this can be done is found in the sampleInvestigate method.

### Objective 4 - Merging and Pushing (everybody submits their own version of the project to their own repos)
- Click on the word "Git" in the bottom left corner of IntelliJ
    - Select and right-click on "main"
        - this option should be under a heading of "Local" NOT "Remote"
- Select "Checkout" on the pop-out after clicking on main under Local Branches
    - This should switch IntelliJ back to the main branch
    - You should see the word "main" in the bottom right corner of the screen now
    - main should also have a little price tag icon next to it in the list on the "Git" window
- Now with `main` selected
    - Highlight and right-click on `dev`
    - Select Merge into Current
- Right click on `main`
    - Select Push
    - Make sure in the window that pops up you see your commits on the left panel and the file you want to push on the right panel
    - Click the `push` button at the bottom
- Right click on `dev`
    - Select `push`
    - On the window that appears click on the `push` button
