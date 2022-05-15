import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.JOptionPane;
import java.lang.reflect.Method;

public class RunAction extends JDialog implements ActionListener
{
    JButton run,cancel,browse;
    JPanel jp1,jp2;
    JTextField txt;

    public RunAction()
    {
        Container cp=getContentPane();

        jp1=new JPanel();
        jp2=new JPanel();

        JLabel lb=new JLabel("Select Program to Run");

        txt=new JTextField(30);

        browse=new JButton(". . .");
        browse.addActionListener(this);

        run=new JButton(" Run ");
        cancel=new JButton(" Cancel ");

        run.addActionListener(this);
        cancel.addActionListener(this);

        jp1.add(lb);
        jp1.add(txt,BorderLayout.CENTER);
        jp1.add(browse,BorderLayout.EAST);

        jp2.add(run);
        jp2.add(cancel);

        cp.add(jp1,BorderLayout.CENTER);
        cp.add(jp2,BorderLayout.SOUTH);

    }

    public void actionPerformed(ActionEvent evt)
    {
        
		Object src=evt.getSource();
        if(src==browse)
        {
            FileDialog fd=new FileDialog(new JFrame(),"Select File",FileDialog.LOAD);
            fd.show();
            if(fd.getFile()!=null)
            {
                String file=fd.getDirectory()+fd.getFile();
                txt.setText(file);
            }
        }
        else if(src==run)
        {
            if(txt.getText()!=null)
            {
                File file = new File(txt.getText());
                if(file.exists())
                {
                    String temp = "";
					if(file.toString().contains(".java"))
                    {
						
                        try
                        {
							 
							 
							
                          // first compiler and see if there are any errors
						  try{
							  Process p = Runtime.getRuntime().exec("javac \""+ file.toString()+"\"");
								//checking for errors
								boolean errorFree = true;
							  BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
								String Error;
								
								while ((Error = stdError.readLine()) != null) {
									errorFree = false;
									temp+=Error;
									temp+="\n";
									
								}
								if(!errorFree){
									JOptionPane.showMessageDialog(null, temp);
								}
								else{
									temp = "";
									
									String direct = file.toString();
									//direct = direct.replace("\\","\\\\");
									System.out.println(direct);
									String filePathH = direct.substring(0,direct.lastIndexOf("\\"));
									
									direct = direct.replace(".java","");
									String fileName = direct.substring(  direct.lastIndexOf("\\")+1, direct.length()  );
									//now getting output
									
									
									
									
									
									System.out.println(filePathH);
									String com = "cmd.exe /c cd \""+filePathH+"\" & start cmd.exe /k \"java " + fileName+ "\" " ;
									//System.out.println(com);
									Process p1 = Runtime.getRuntime().exec(com);
									//cmd /c cd \"C:\\Users\\mrhas\\OneDrive\\Desktop\\Testing Editor\\Testing Java code\" && java Hassan
									//checking for errors
										
									
							
									
									
									
								}
								
						  }
						  catch(Exception e){
							  e.printStackTrace();
						  }
                        }
                        catch(Exception e){ }
						
                    }
                    else
                    {
                       JOptionPane.showMessageDialog(null, "Please select  .java file\n asdas\nsdfs\nsdfsd\n");
                    }
                }
            }
            dispose();
        }
        else if(src==cancel)
        {
            dispose();
        }
    }
	
	

}
