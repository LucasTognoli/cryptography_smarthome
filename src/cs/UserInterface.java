package cs;

import java.io.File;
import java.io.IOException;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class UserInterface extends JPanel {

    private static final long serialVersionUID = 1L;
    private boolean flag1 = false, flag2 = false;

    private static JTextField keyField;
    private JTextField filename = new JTextField(), dir = new JTextField(), output = new JTextField();
    
    private File input_file;
    private File output_folder;
    
    public UserInterface() {
        setLayout(new BorderLayout());

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new FlowLayout());

        JLabel label = new JLabel("Public Key ");
        northPanel.add(label);

        keyField = new JTextField(15);
        northPanel.add(keyField);
        add(northPanel, BorderLayout.NORTH);

        // --- Buttons

        JPanel centerPanel = new JPanel();  
        JButton btn = new JButton("Encrypt");
        btn.addActionListener(new BtnListener());
        centerPanel.add(btn);
        
        JButton btn2 = new JButton("Decrypt");
        btn2.addActionListener(new BtnListener2());
        centerPanel.add(btn2);


        add(centerPanel, BorderLayout.SOUTH);
        
        //-------- Input Browser
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        
        JButton input_btn = new JButton("Input File");
        input_btn.addActionListener(new OpenL());
        inputPanel.add(input_btn);
        filename.setText("    Select the Input File (.txt)    ");
        filename.setEditable(false);
        inputPanel.add(filename);
        
        //-------- Output Path Browser
        
        JPanel outputPanel = new JPanel();
        outputPanel.setLayout(new FlowLayout());
        
        JButton output_btn = new JButton("Output Path");
        output_btn.addActionListener(new OpenPath());
        outputPanel.add(output_btn);
        output.setText("    Select the Output Path    ");
        output.setEditable(false);
        outputPanel.add(output);

        // ----- South panel
        
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        southPanel.add(inputPanel);
        southPanel.add(outputPanel);
        add(southPanel, BorderLayout.CENTER);
    }

    private class BtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String content = ""; 
            content = keyField.getText();

                //@SuppressWarnings("unused")

                if(content.length()!= 128){
                	infoBox("Invalid key length", "Error");
                }
                if(!flag1){
                	infoBox("You must select an Input File (.txt)", "Error");
                }
                if(!flag2){
                	infoBox("You must select an Output Folder", "Error");
                }
                
                if(flag1 && flag2 && content.length() == 128){
                	try {
						Cryptography.encrypt(content, input_file, output_folder);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                	infoBox("You can find the Encrypted file on the output folder", "Success");
                }
        }
        public void infoBox(String infoMessage, String titleBar)
        {
            JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    //Decrypt Button
    private class BtnListener2 implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
        	String content = ""; 
            content = keyField.getText();
        	if(content.length()!= 128){
            	//System.out.println("Insert a valid Public Key");
            	infoBox("Invalid key length", "Error");
            }
                if(!flag1){
                	//System.out.println("You must select an Input File (.txt)");
                	infoBox("You must select an Input File (.txt)", "Error");
                }
                if(!flag2){
                	//System.out.println("You must select an Output Folder");
                	infoBox("You must select an Output Folder", "Error");
                }
                
                if(flag1 && flag2 && content.length() == 128){
                	try {
						Cryptography.decrypt(content, input_file, output_folder);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                	infoBox("You can find the Decrypted file on the output folder", "Success");
                }
        }
        public void infoBox(String infoMessage, String titleBar)
        {
            JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    class OpenL implements ActionListener {
        public void actionPerformed(ActionEvent e) {
          JFileChooser c = new JFileChooser();
          // Demonstrate "Open" dialog:
          int rVal = c.showOpenDialog(UserInterface.this);
          if (rVal == JFileChooser.APPROVE_OPTION) {
        	  if(c.getSelectedFile().getName().endsWith(".txt")){
        		filename.setText(c.getSelectedFile().getName());
        	  	dir.setText(c.getCurrentDirectory().toString());
        	  	flag1 = true;
        	  	input_file = c.getSelectedFile();
        	  }
        	  else{
        		  infoBox("Select a .txt file", "Error"); 
        	  }
            
          }
          if (rVal == JFileChooser.CANCEL_OPTION) {}
        }
        public void infoBox(String infoMessage, String titleBar)
        {
            JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
        }
      }
    
    class OpenPath implements ActionListener {
    	JFileChooser chooser = new JFileChooser();
    	String choosertitle;
    	
    	public void actionPerformed(ActionEvent e) {
          
    		chooser.setCurrentDirectory(new java.io.File("."));
    	    chooser.setDialogTitle(choosertitle);
    	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

    	    chooser.setAcceptAllFileFilterUsed(false);   
    	    if (chooser.showOpenDialog(UserInterface.this) == JFileChooser.APPROVE_OPTION) { 
    	    	output.setText(chooser.getSelectedFile().toString());
    	    	flag2 = true;
    	    	output_folder = chooser.getSelectedFile();
    	    	
    	    }
    	    else {
    	      System.out.println("No Selection ");
    	    }
    	}
   }
    
}
