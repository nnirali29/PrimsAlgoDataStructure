/*************
* Programmer Name: Nirali Patel
* Course: CSCI 5432
* Date: April 7, 2020
* Assignment: Assignment #3
* Environment: Command Prompt
* Files Included: 
* - AdjacencyListGraphPrims.java
* Purpose of this code: 
* Get the type of graph (Directed or Undirected, number of node, nodeLabels,number of edges and Enter the 
* Edges in Graph Format : <Edge_Label> <Source_Node> <Destination_Node> <Weight>.
* Using adjacency list implement the graph and ask from the user below mentioned things and get the output
* according to that.
* 1.If you input a node label, it should output in- and out-degree for the node.
* 2.If you input an edge label, it should output its source and destination edge.
* 3.If you input a node label, it should output all incident edges for the node. 
* 4.Implement Prim's Algorithm and use problem 2 graph as an input to construct minimum spanning tree
* and find it's cost.
* Prim's Algorithm:
*
* We will take graph and a starting vertex as an initial input.
* 1.  First we will make a queue with all the nodes of the graph.
* 2.  Set the priority to INFINITY for each member of queue .
* 3.  set the priority to for the starting node only.
* 4.  The parent of node should be NULL.
* 5.  While queue is not empty
* 6.     Get the minimum from queue – let’s say (a); (priority queue);
* 7.     For each adjacent node to (v) to (a)
* 8.        If (v) is in queue and weight of (a, v) < priority of (v) then
* 9.           The parent of (v) is set to be (a)
* 10.          The priority of (v) is the weight of (a, v)
* 
* --------------------------------------------
* Adjacency List Data Structure Implementation
* --------------------------------------------
* In this we represent a directed graph who has N number of nodes with an array of n number of list nodes. 
* In this if list "a" contains a node "p" and if edge is from node "a" to node "p"
* Then weighted graph will be represented with a list of node and weight combination. 
* or an undirected graph will be represented by node "p" in the list for node "a" 
* and node "a" in the list for node "p".
* 
* For Example:-
*    1 2 3 4
* 1  0 0 0 1			1  -> 4
* 2  1 1 0 1	==		2  -> 1 -> 2 -> 4
* 3  1 1 1 1			3  -> 1 -> 2 -> 3 -> 4
* 4  0 1 1 0			4  -> 2 -> 3
***********************************************************************/
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class AdjPrims
{
	int cost;
	int[] keynode;
	int[] nodeadd;
	static Node[] node;

	public AdjPrims(int vert) 
	{
		cost = 0;
		keynode = new int[vert];
		for (int i = 0; i < vert; i++)
			keynode[i] = Integer.MAX_VALUE;
		nodeadd = new int[vert];
	}

	static class Node // This class is for the properties of node
	{
		LinkedList<Node> adjacency;
		int value;
		Node parentnode;

		public Node(int data)
		{
			value = data;
			parentnode = null;
			adjacency = new LinkedList<>();
		}
	}

	static class Graph //This class is for to represent the graph and the graph will be adjacency list array.
	{ 
		int Nodesnumber;
		LinkedList<Integer> adjListArray[];
		LinkedList<Integer> incidentArray[];
		LinkedList<Integer> inDegreeArray[];
		LinkedList<Integer> outDegreeArray[];

		@SuppressWarnings("unchecked")
		Graph(int Nodes) 
		{
			Nodesnumber = Nodes;

			node = new Node[Nodesnumber];
			adjListArray = new LinkedList[Nodesnumber];
			incidentArray = new LinkedList[Nodesnumber];
			inDegreeArray = new LinkedList[Nodesnumber];
			outDegreeArray = new LinkedList[Nodesnumber];

			for (int i = 0; i < Nodesnumber; i++)
			{
				node[i] = new Node(i);
				adjListArray[i] = new LinkedList<>();
				incidentArray[i] = new LinkedList<>();
				inDegreeArray[i] = new LinkedList<>();
				outDegreeArray[i] = new LinkedList<>();
			}
		}
	}

	void edgeadd(Graph g, int source, int destination, int edgelabel, char Gtype) 
	{
		if (Gtype == 'A')
			{
			node[source].adjacency.add(node[destination]);
			if (source != destination) 
			{
				node[destination].adjacency.add(node[source]);
			}
			addUndirectedEdge(g, source, destination);
			} 
		else if (Gtype == 'B')
			{
			node[source].adjacency.add(node[destination]);
			addDirectedEdge(g, source, destination);
			} 
		else 
			{
			 System.out.println("you can only give input as A or B");
			}
	}

	void incidentedge(Graph i, int source, int destination, int edgelabel) 
	{
		i.incidentArray[destination].addFirst(edgelabel);
		i.incidentArray[source].addFirst(edgelabel);
	}

	void inoutdegree(Graph Degree, int source, int destination, int edgelabel) 
	{
		Degree.inDegreeArray[destination].addFirst(edgelabel);
		Degree.outDegreeArray[source].addFirst(edgelabel);
	}

	void addUndirectedEdge(Graph g, int source, int destination) 
	{
		g.adjListArray[source].addFirst(destination);
		g.adjListArray[destination].addFirst(source);
	}

	void addDirectedEdge(Graph g1, int source, int destination) 
	{
		g1.adjListArray[source].addFirst(destination);
	}

	void graphprint(String[] nodeLabel, Graph g2) 
	{
		for (int j = 0; j < g2.Nodesnumber; j++) 
		{
			System.out.print("Node's Adjacency list " + nodeLabel[j] + ": ");
			for (Integer a : g2.adjListArray[j]) 
			{
				System.out.print(nodeLabel[a] + " ");
			}
			System.out.println("\n");
		}
	}

	void incidentedgeprint(String[] nodeLabel, String[] edgeLabel, Graph i, int index) 
	{
		System.out.println("Node's incident list " + nodeLabel[index] + ": ");
		for (Integer incident : i.incidentArray[index]) 
		{
			System.out.print(edgeLabel[incident] + " ");
		}
		System.out.println("\n");
	}

	void inoutdegree1(String[] label, Graph degree, int node) 
	{
		int indegree = 0, outdegree = 0;
		System.out.print("Out-Degree of Node " + label[node] + ": ");
		for (Integer outdgr : degree.outDegreeArray[node]) 
		{
			outdegree++;
		}
		System.out.println(outdegree);
		
		System.out.print("Node's In Degree " + label[node] + ": ");
		for (Integer indgr : degree.inDegreeArray[node]) 
		{
			indegree++;
		}
		System.out.println(indegree + "\n");
	
	}
	
	int findmin(int node) 
	{
		int minimum = Integer.MAX_VALUE;
		int position = -1;
		for (int i = 0; i < node; i++) 
		{
			if (nodeadd[i] == 0 && keynode[i] < minimum) 
			{
				minimum = keynode[i];
				position = i;
			}
		}
		return position;
	}

	void prims(int[][] weight, int first, int vertex, String[] nodeLabel) //Prim's Algorithm
	{
		keynode[first] = 0;
		node[first].parentnode = null;
		int minNode = findmin(vertex);
		while (minNode != -1) 
		{
			cost += keynode[minNode];
			System.out.print("Node[" + nodeLabel[minNode] + "]");
			System.out.print(" ............ ");
			System.out.print("Weight[" + keynode[minNode] + "]");
			System.out.print(" ......... ");
			System.out.println("Cost[" + cost + "]");
			for (Node vert : node[minNode].adjacency) 
			{
				if (nodeadd[vert.value] == 0 && weight[minNode][vert.value] < keynode[vert.value]) 
				{
					vert.parentnode = node[minNode];
					keynode[vert.value] = weight[minNode][vert.value];
				}
			}
			nodeadd[minNode] = 1;
			minNode = findmin(vertex);
		}
		System.out.println("\n Cost of the minimum spanning tree:-> " + cost);
	}

	public static void main(String args[]) 
	{
		int number = 1;
		String sou, dest1;
		String edge, node;
		int nodes, edges, weight;
		char Gtype = 'A';
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Select Graph Type : A - Directed , B - Undirected");
		Gtype = sc.next().charAt(0);
		System.out.println("Enter node numbers:-> ");
		nodes = sc.nextInt();
		String nodearray[] = new String[nodes];
		System.out.println("Enter the Labels of the node:->");
		while (number <= nodes) 
		{
			node = sc.next();
			nodearray[number - 1] = node;
			number++;
		}
		System.out.println("Enter edge numbers:-> ");
		edges = sc.nextInt();
		String edgea[] = new String[edges];

		HashMap<String, String> hmap = new HashMap<String, String>();
		Graph graph = new Graph(nodes);
		System.out.println("Enter the Edges in Graph Format : <Edge_Label> <Source_Node> <Destination_Node> <Weight>");
		AdjPrims algprim = new AdjPrims(nodes);
		int[][] warray = new int[nodes][nodes];
		for (int i = 0; i < edges; i++)
		{
			edge = sc.next();
			sou = sc.next();
			dest1 = sc.next();
			weight = sc.nextInt();
			edgea[i] = edge;
			hmap.put(edge, sou + "->" + dest1);
			int sourceindex = Arrays.asList(nodearray).indexOf(sou);
			int destindex = Arrays.asList(nodearray).indexOf(dest1);
			warray[sourceindex][destindex] = weight;
			warray[destindex][sourceindex] = weight;
			algprim.edgeadd(graph, sourceindex, destindex, i, Gtype);
			algprim.inoutdegree(graph, sourceindex, destindex, i);
			algprim.incidentedge(graph, sourceindex, destindex, i);
		}
		System.out.println("\n");
		System.out.println("*** Graph representated in an Adjacency List ***");
		algprim.graphprint(nodearray, graph);
		System.out.println("(a) Enter the Node Label to output in- and out-degree for the node:");
		node = sc.next();
		algprim.inoutdegree1(nodearray, graph, Arrays.asList(nodearray).indexOf(node));
		System.out.println("(b) Enter the Edge Label to output its source and destination nodes:");
		edge = sc.next();
		System.out.println("Edge " + edge + " : " + "Source --> Destination : " + hmap.get(edge) + "\n");
		System.out.println("(c) Enter the Node Label to output all incident edges for the node:");
		node = sc.next();
		algprim.incidentedgeprint(nodearray, edgea, graph, Arrays.asList(nodearray).indexOf(node));
		System.out.println("(d) Executing Prims Implementation for this Graph...");
		int start = 0;
		algprim.prims(warray, start, nodes, nodearray);
		sc.close();
	}
}
