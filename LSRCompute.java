import java.io.*;
import java.util.PriorityQueue;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

class Vertex implements Comparable<Vertex>
{
    public final String name;
    public Edge[] adjacencies;
    public double minDistance = Double.POSITIVE_INFINITY;
    public Vertex previous;
    public Vertex(String argName) { name = argName; }
    public String toString() { return name; }
    public int compareTo(Vertex other)
    {
        return Double.compare(minDistance, other.minDistance);
    }

}

class Edge
{
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
            // Visit each edge exiting u
            for (Edge e : u.adjacencies){
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
            System.err.println("Error: " + e.getMessage());
        }
        
        // create all nodes by total number
        Vertex[] tmpVertex = new Vertex[totalNodes];
        for(i=0; i<totalNodes; i++){
            // create new node(A,B,C...), assign it into tmpVertex
            tmpVertex[i] = new Vertex(nodeName[i]);
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
                Edge tmpNB[] = new Edge[aLineOfrecord.length-1]; // -1 exclude itself
                for(int neighbors=1; neighbors<aLineOfrecord.length; neighbors++){
                    
                    int nb = Arrays.asList(nodeName).indexOf( aLineOfrecord[neighbors].substring(0,1));
                    double cost = Double.parseDouble( aLineOfrecord[neighbors].substring(2,3) );
                    //System.out.println( tmpVertex[nb] + " " +  aLineOfrecord[neighbors].substring(2,3));                    
                    
                    tmpNB[neighbors-1] = new Edge(tmpVertex[nb],  cost);
                }
                tmpVertex[j++].adjacencies = tmpNB;          
                
            }             

            //Close the input stream
            br.close();
        }
        catch(Exception e){//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
        
        int src = 0;
        src = Arrays.asList(nodeName).indexOf( args[1] );
        
        switch(args[2].toUpperCase()){
            case "CA":
                computePaths(tmpVertex[src]);
                System.out.println( "Source " + tmpVertex[src] + ":" );
                for (Vertex v : tmpVertex)
                {   
                    if(v!=tmpVertex[src]){
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
                break;
            
            case "SS":
                computePaths(tmpVertex[src]);
                System.out.println( "Source " + tmpVertex[src] + ":" );
                
                while(true){
                    System.out.print("To which node? ");
                    Scanner sc = new Scanner(System.in);
                    String inputDes = sc.next().toUpperCase();
                    
                    Vertex v = tmpVertex[Arrays.asList(nodeName).indexOf( inputDes )];
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
        System.out.print(" [Press any key to continue]");
        try {
            System.in.read();
        }  
        catch(Exception e)
        {}  
    }
}
