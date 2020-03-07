import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Queue;
public class Main {
  public static void main(String[] args) throws IOException {
    Scanner sc = new Scanner(System.in);
    System.out.println("How many files do you have? Name these files as week1, week2, week3, etc.");
    int num = sc.nextInt();
    //ArrayList sotrs all the sorted queues for later use
    ArrayList<Queue<String>> Queues = new ArrayList<Queue<String>>();
    //since num is the total number of files there are, this for loop generates the file names
    //based on the value of num and runs them through sort. Afterwards, the sorted queue is stored in Queues
    for(int i=0; i<num; i++) {
      Queues.add(Sort("week"+(i+1)+".csv", (i+1)));
    }
    //generic object of the PlayLIst class
    PlayList printList = new PlayList(Queues, num);
    //printPlayList is a method of PlayList which prints the entire playlist to a txt file
    printList.printPlayList();
  }
  //takes the name of a file and then proceeds to take the name of each song and put it in an array
  //the array that is now full of song titles is then Sorted alphabetically and stored into a queue
  public static Queue<String> Sort(String name, int x) throws IOException {
    File file = new File(name);
    Scanner sc = new Scanner(file);
    int arraySize = 0;
    while (sc.hasNext()) {
      arraySize++;
      sc.nextLine();
    }
    sc = new Scanner(file);
    //takes the name of the song and stores in the array
    String[] arr = new String[arraySize];
    for (int i=0; i<arraySize; i++) {
      arr[i] = sc.nextLine();
      if (arr[i].substring(1, 4).contains("\"")) {
        int end = arr[i].indexOf("\"", arr[i].lastIndexOf("\""));
        arr[i] = arr[i].substring(arr[i].indexOf("\"")+1, end);
      }
      else {
        int end = arr[i].indexOf(",", arr[i].indexOf(",")+1);
        arr[i] = arr[i].substring(arr[i].indexOf(",")+1, end);
      }
    }
    //alphabetizes the array
    String temp = "";
    for (int i=0; i<arr.length; i++) {
      for (int j=0; j<arr.length; j++) {
        if (arr[i].compareToIgnoreCase(arr[j])<0) {
          temp = arr[i];
          arr[i] = arr[j];
          arr[j] = temp;
        }
      }
    }
    //converts the array to a queue
    Queue<String> Q = new LinkedList<>();
    for (int i=0; i<arr.length; i++) {
      Q.add(arr[i]);
    }    
    return Q;
  } 
}
//generates the playlist by taking all the queues in the arraylist and merging them into one big queue
//then prints the entire playlist to a txt file
class PlayList {
  int Size;
  Queue<String> playList;
  Stack<String> stack;  
  PlayList(ArrayList<Queue<String>> Queues, int num) throws IOException {
    playList = merge(Queues.get(0), Queues.get(1));
    if (num > 2) {
      for (int i=2; i<num; i++) {
        playList = merge(playList, Queues.get(i));        
      }
    }
    Size = playList.size();
    stack = new Stack<String>();    
  }
  //prints the entire playlist to a txt file
  void printPlayList() throws IOException {
    PrintWriter output = new PrintWriter("Output.txt");
    for (int i=0; i<Size; i++) {
      output.println(playList.peek());
      stack.push(playList.poll());
    }
    output.close();
  }
  //merges all the queues into one big queue
  Queue<String> merge(Queue<String> first, Queue<String> second) {
    Queue<String> mergedQueue = new LinkedList<String>();
    // If both queues are not empty.
    while (!first.isEmpty() && !second.isEmpty()) {
      String q1 = first.peek();
      String q2 = second.peek();
      if (q1.compareToIgnoreCase(q2)<0) {
        mergedQueue.add(first.poll());
      } 
      else {
        mergedQueue.add(second.poll());
      }
    }
    // If there are remaining items in one of the queue.
    while (!first.isEmpty()) {
      mergedQueue.add(first.poll());
    }
    while (!second.isEmpty()) {
      mergedQueue.add(second.poll());
    }
    return mergedQueue;
  }  
}
