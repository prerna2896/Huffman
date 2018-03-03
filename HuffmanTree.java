// PRERNA AGARWAL
// NIT DURGAPUR

import java.io.*;
import java.util.*;
// This class encodes the contents of a file using Huffman code and then store stores the encoded
// file. It then decodes the encoded file using the stored coding scheme

public class HuffmanTree {
	private HuffmanNode overallRoot;
	
	// This class creates the Huffman nodes which forms the binary tree which is used for encoding
	private class HuffmanNode implements Comparable<HuffmanNode> {
		private int frequency;
		private int character;
		private HuffmanNode left;
		private HuffmanNode right;
		
		// This method initializes a leaf node for the huffman tree
		private HuffmanNode(int frequency, int character) {
			this(frequency, character, null, null);
		}
		
		// This method initializes a node for the huffman tree
		private HuffmanNode(int frequency, int character, HuffmanNode left, HuffmanNode right) {
			this.frequency = frequency;
			this.character = character;
			this.left = left;
			this.right = right;
		}
		
		// This method compares two nodes on the basis of their frequencies and returns 1 if 
		// the first one has the greater frequency and 0 otherwise
		public int compareTo(HuffmanNode other) {
			return (this.frequency - other.frequency);
		}
		
		// This method checks if the node is a leaf node
		public boolean isLeaf() {
			return (this.right == null && this.left == null);
		}
	}
	
	// This method creates a Huffman tree using priority queue using the array of frequency count
	// passed by the user
	public HuffmanTree(int[] counts) {
		PriorityQueue<HuffmanNode> queue = new PriorityQueue<HuffmanNode>();
		for (int i = 0; i < counts.length; i++) {
			if (counts[i] > 0) {
				queue.add(new HuffmanNode(counts[i], i));
			}
		}
		queue.add(new HuffmanNode(1, (counts.length)));
		while (queue.size() != 1) {
			HuffmanNode left = queue.remove();
			HuffmanNode right = queue.remove();
			HuffmanNode root = new HuffmanNode((left.frequency + right.frequency), 1,
												left, right);
			queue.add(root);
		}
		overallRoot = queue.remove();
	}
	
	// This method writes the tree to the given output stream with the ASCII value of the character
	// in one line followed by its respective code in the next
	public void write(PrintStream output) {
		String codes = "";
		add(overallRoot, codes, output);
	}
	
	// This private method assists the print method to write the codes of all the characters
	public void add(HuffmanNode root, String codes, PrintStream output) {
		if (root.isLeaf()) {
			output.println(root.character);
			output.println(codes);
		} else {
			add(root.left, codes + "0", output);
			add(root.right, codes + "1", output);
		}
	}
	
	// This method reconstructs a tree form the code file, assuming that the scanner contains
	// the tree in a standard format
	public HuffmanTree(Scanner input) {
		overallRoot = new HuffmanNode(1, '1');
		while(input.hasNextLine()) {
			int character = (Integer.parseInt(input.nextLine()));
			String code = input.nextLine();
			scan(character, code, overallRoot);
		}
	}
	
	// This private method helps huffman tree class to reconstruct a huffman from a file
	private void scan(int character, String code, HuffmanNode root) {
		if (code.equals("")) {
			root.character = character;
		} else {
			if (code.charAt(0) == '0') {
				if (root.left == null) {
					root.left = new HuffmanNode(1, '1');
				}
				scan(character, code.substring(1), root.left);
			} else {
				if (root.right == null) {
					root.right = new HuffmanNode(1, '1');
				}
				scan(character, code.substring(1), root.right);
			}
		}
	}
	
	// This method reads the encoded file and prints its decoded version until it encounters 
	// a character identical to the value of the eof
	public void decode(BitInputStream input, PrintStream output, int eof) {
		breakCode(input, output, overallRoot, eof);
	}
	
	// This private method helps to decode the encoded file and print it
	public void breakCode(BitInputStream input, PrintStream output, HuffmanNode root, int eof) {
		while (1 == 1) {
			if (root.isLeaf()) {
				if (root.character != eof) {
					output.print((char)root.character);
				} else {
					return;
				}
				root = overallRoot;
			}
			if (input.readBit() == 0) {
				root = root.left;
			} else {
				root = root.right;
			}
		}
	}
}