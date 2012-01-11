/* Based off of Obama McCain example from
 * http://en.wikipedia.org/wiki/Processing_%28programming_language%29
 *
 * parses reindexed-tweets.txt and displays positive/negative tweet ratio
 * more blue -> more positive, more red -> more negative
 * 
 * Todd Bodnar
 *
 * Changelog:
 * Jan 9, 2012
 *     initial code
 *
 */
 
 PShape usa;
 // for score, we add one for each positive, subtract one for each negative
 // for tweets, we only count positive/negative tweets
 int score[], tweets[];
 
 String statenames[] = {"HI", "RI", "CT", "MA", "ME", "NH", "VT", "NY", "NJ",
         "FL", "NC", "OH", "IN", "IA", "CO", "NV", "PA", "DE", "MD", "MI",
         "WA", "CA", "OR", "IL", "MN", "WI", "DC", "NM", "VA" , "AK", "GA", 
         "AL", "TN", "WV", "KY", "SC", "WY", "MT",
         "ID", "TX", "AZ", "UT", "ND", "SD", "NE", "MS", "MO", "AR", "OK",
         "KS", "LA" };
 
 
 /* returns the location of id in statenames, or -1 if not found */
 int getId(String id)
 {
   for(int i=0;i<statenames.length;i++)
      if(statenames[i].equals(id))
         return i;
         
    return -1;
 }
 
 void parseData(){
   String[] dataset = loadStrings("/Users/toddbodnar/Data/tweets/h1n1/reindexed-tweets.txt"); //todo, look up name

   
   score = new int[statenames.length];
   tweets = new int[statenames.length];
   
   for(int i=0;i<score.length;i++){
     score[i]=0;
     tweets[i]=0;
   }
   
   for(int i=0;i<dataset.length;i++){
     
     try{
       
       StringTokenizer st = new StringTokenizer(dataset[i],"\t");
       st.nextToken();//tweet_id
       st.nextToken();//time
       st.nextToken();//tweeter
       String country = st.nextToken();
       String state_id = st.nextToken();
       String classification = st.nextToken();
        
     
       if(country.equals("US"))
       {
           
          int state_id_num = getId(state_id);
          if(state_id_num>=0 && classification!="O")
          {
             tweets[state_id_num]++;
             if(classification.equals("+"))
               score[state_id_num]++;
             else
               score[state_id_num]--;
          }
       }
     
     }catch(NoSuchElementException e){;} //acceptable exception because some tweets lack all fields
   }
 }
 
 void setup(){      
   size(950,600);
   usa = loadShape("http://upload.wikimedia.org/wikipedia/commons/3/32/Blank_US_Map.svg");
 
   parseData();
   noLoop(); //disable continually calling draw
 }
 
 void draw(){
   shape(usa,0,0);
   
   double minscore = 10;
   double maxscore = -10;
   for(int i=0;i<statenames.length;i++){
     double value = 1.0*score[i]/tweets[i];
     
     if(value<minscore)
       minscore=value;
     if(value>maxscore)
       maxscore=value;
   }
   
   for(int i=0;i<statenames.length;i++){
     PShape state = usa.getChild(statenames[i]);
     state.disableStyle(); //disable the file's style
     double value = 1.0*score[i]/tweets[i];
     
     //transform value
     value = (value - minscore)/(maxscore-minscore)*2-1;
     
     println(statenames[i]+":  "+value);
     
     if(value>0)
       fill(255 - (int)(255*value),255-(int)(255*value),255); //note, 0.0f - 1.0f does not work
     else
        fill(255,255 + (int)(255*value),255 + (int)(255*value));
        
     shape(state,0,0);
   }
 }
