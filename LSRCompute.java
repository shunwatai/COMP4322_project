import java.io.*;
import java.util.PriorityQueue;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

class Vertex implements Comparable<Vertex>{
    public final String name;
    public ArrayList<Edge> adjacencies;
    public double minDistance = Double.POSITIVE_INFINITY;
    public Vertex previous;
    public Vertex(String argName) { name = argName; }
    public String toString() { return name; }
    public int compareTo(Vertex other)
    {
        return Double.compare(minDistance, other.minDistance);
    }
}

class Edge{
    public final Vertex target;
    public final double weight;
    public Edge(Vertex argTarget, double argWeight)
    { target = argTarget; weight = argWeight; }
}


class LSRCompute{
    public static void computePaths(Vertex source) {
        source.minDistance = 0.;
        PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
        vertexQueue.add(source);

        while (!vertexQueue.isEmpty()) {
            Vertex u = vertexQueue.poll();
            //System.out.print("u: "+u);
            // Visit each edge exiting u
            for (Edge e : u.adjacencies){
                //System.out.print(" e: "+e.target.name);
                Vertex v = e.target;
                double weight = e.weight;
                double distanceThroughU = u.minDistance + weight;
                if (distanceThroughU < v.minDistance) {
                    vertexQueue.remove(v);

                    v.minDistance = distanceThroughU ;
                    v.previous = u;
                    vertexQueue.add(v);
                }
            }
        }
    }
    public static List<Vertex> getShortestPathTo(Vertex target) {
        List<Vertex> path = new ArrayList<Vertex>();
        for (Vertex vertex = target; vertex != null; vertex = vertex.previous)
            path.add(vertex);

        Collections.reverse(path);
        return path;
    }


    public static void main(String[] args){
        System.out.println("test");

        String[] nodeName = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};

        int totalNodes = 0;
        int i = 0;
        int j = 0;

        try{
            LineNumberReader  lnr = new LineNumberReader(new FileReader(new File("routes.lsa")));
            lnr.skip(Long.MAX_VALUE);
            //totalNodes = lnr.getLineNumber() + 1; //Add 1 because line index starts at 0
            totalNodes = lnr.getLineNumber();
            lnr.close();
        }catch(Exception e){//Catch exception if any
            System.err.println("Errors: " + e.getMessage());
        }

        // create all nodes by total number
        ArrayList<Vertex> tmpVertex = new ArrayList<Vertex>();
        for(i=0; i<totalNodes; i++){
            // create new node(A,B,C...), assign it into tmpVertex
            tmpVertex.add(new Vertex(nodeName[i]));
        }

        try{
            // Open the file
            FileInputStream fstream = new FileInputStream(args[0]);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            String strLine;
            i = 0;  //
            int addNeighbor = 0;
            //Read File Line By Line, for create neighbors
            while ((strLine = br.readLine()) != null){
                // split by spaces store in aLineOfrecord
                String[] aLineOfrecord = strLine.split("\\s+");

                // create neighbors array
                ArrayList<Edge> tmpNB = new ArrayList<Edge>(); 

                for(int neighbors=1; neighbors<aLineOfrecord.length; neighbors++){ // start at 1, exclude 0(newNode itself)
                    int nb = Arrays.asList(nodeName).indexOf( aLineOfrecord[neighbors].substring(0,1)); // take out the neighbor as alphabet
                    double cost = Double.parseDouble( aLineOfrecord[neighbors].substring(2,3) );  // take the neighbor cost
                    tmpNB.add( new Edge(tmpVertex.get(nb), cost) );  // add into the neighbor array
                }
                tmpVertex.get(j++).adjacencies = tmpNB;  // make the relation betwwen the newNode and its neighbors
            }

            //Close the input stream
            br.close();
        }
        catch(Exception e){//Catch exception if any
            System.err.println("Errorss: " + e.getMessage());
        }

        int src = 0;
        src = Arrays.asList(nodeName).indexOf( args[1] );

        switch(args[2].toUpperCase()){
            case "CA":
                while(true){
                    computePaths(tmpVertex.get(src));
                    System.out.println( "Source " + tmpVertex.get(src) + ":" );

					for (Vertex v : tmpVertex)
					{
						//if(v!=tmpVertex[src]){
                        if(v!=tmpVertex.get(src)){
							System.out.print( v + ": " );
							List<Vertex> path = getShortestPathTo(v);
							System.out.print("Path: ");
							for(i = 0; i < path.size(); i++) {
								System.out.print( path.get(i) );
								if (i<path.size()-1)
									System.out.print( ">" );
							}
							System.out.println(" Cost:"+ v.minDistance);
						}
					}
                    addNode(tmpVertex, nodeName);

                    for (Vertex v : tmpVertex){ // reset all the distance
                        v.minDistance = Double.POSITIVE_INFINITY;
                    }

					pressAnyKeyToContinue();
				}
                //break;

            case "SS":
                computePaths(tmpVertex.get(src));
                System.out.println( "Source " + tmpVertex.get(src) + ":" );

                while(true){
                    System.out.print("To which node? ");
                    Scanner sc = new Scanner(System.in);
                    String inputDes = sc.next().toUpperCase();

                    Vertex v = tmpVertex.get( Arrays.asList(nodeName).indexOf( inputDes ) );
                    System.out.print( v + ": " );
                    List<Vertex> path = getShortestPathTo(v);
                    System.out.print("Path: ");
                    for(i = 0; i < path.size(); i++) {
                        System.out.print( path.get(i) );
                        if (i<path.size()-1)
                            System.out.print( ">" );
                    }
                    System.out.print(" Cost:"+ v.minDistance);
                    pressAnyKeyToContinue();
                }
        }
    }

    private static void pressAnyKeyToContinue(){
        System.out.println(" [Press any key to continue]");
        try {
            System.in.read();
        }
        catch(Exception e){
            System.err.println("Errorss: " + e.getMessage());
        }
    }

    // for add new node
    private static ArrayList<Vertex> addNode(ArrayList<Vertex> vertex, String[] nodes){
        System.out.print("do you want to add a new node (y/n)? ");
        Scanner sc = new Scanner(System.in);
        String ans = sc.next().toUpperCase(); // for Y or N

        if(ans.equals("Y")){
            System.out.println("please type the new node relation (newNode: existNode1:cost existNode2:cost...):");
            sc = new Scanner(System.in);
            String newRecords = sc.nextLine().toUpperCase(); // for new records
            String[] aLineOfrecord = newRecords.split("\\s+"); // split by space
            // create neighbors array
            ArrayList<Edge> tmpNB = new ArrayList<Edge>();

            // new node
            vertex.add( new Vertex(aLineOfrecord[0].substring(0,1)) );

            // add neighbors
            for(int neighbors=1; neighbors<aLineOfrecord.length; neighbors++){
                int nb = Arrays.asList(nodes).indexOf( aLineOfrecord[neighbors].substring(0,1));
                double cost = Double.parseDouble( aLineOfrecord[neighbors].substring(2,3) );

                //System.out.println("tmpNB.add( new Edge("+vertex.get(nb)+","+cost+") )");
                tmpNB.add( new Edge(vertex.get(nb), cost) );
                //System.out.println(vertex.get(nb) + ".adjacencies.add(" + vertex.get(vertex.size() - 1) +", "+ cost + ")");
                vertex.get(nb).adjacencies.add( new Edge(vertex.get(vertex.size() - 1), cost) );
            }
            vertex.get(vertex.size() - 1).adjacencies = tmpNB;
            //System.out.println(vertex.get(5).adjacencies.get(1).target+":"+vertex.get(5).adjacencies.get(1).weight);
            //G: B:2 F:3
        }
        else{
            
        }

        return vertex;
    }
}
