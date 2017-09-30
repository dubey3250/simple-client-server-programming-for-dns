import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.awt.Desktop;
import java.util.*;
import java.io.IOException;
import java.awt.event.*;
public class eclient extends JFrame implements ActionListener
{
	JButton b1,b2,b3,b4,b5;
	JTextField t1,t2,t3;
	JLabel l1,l2,l3,l4,l5;
	Socket s;
	DataOutputStream dout;
	InetAddress address;
	String hostid,hostn;
	eclient()
	{
		//l1=new JLabel("");
		super("Client.......");
		b1=new JButton("Find IP");
		b2=new JButton("");
		l1=new JLabel("Domain:");
		l2=new JLabel("");
		l3=new JLabel("");
		l4=new JLabel("");
		t1=new JTextField("Enter the domain name..");
		 getContentPane().setBackground(Color.pink);
		 l1.setForeground(Color.blue);
		  l2.setForeground(Color.blue);
		   l3.setForeground(Color.blue);
		   l1.setFont (new Font ("Times New Roman", Font.BOLD,20));
		   l2.setFont (new Font ("Times New Roman", Font.BOLD,18));
		   l3.setFont (new Font ("Times New Roman", Font.BOLD,18));
		   b1.setBackground(Color.blue);
		   b1.setForeground(Color.green);
		add(b1);
		add(b2);
		add(t1);
		add(l1);
		add(l2);
		add(l3);
		add(l4);
		t1.setBounds(100,100,400,40);
		l1.setBounds(10,100,90,40);
		b1.setBounds(500,100,70,40);
		l2.setBounds(150,160,400,30);
		l3.setBounds(150,200,400,30);
		b1.addActionListener(this);
		b2.addActionListener(this);
		setLayout(null);
		setVisible(true);
		setSize(700,700);
		try{
		s=new Socket("localhost",6666); 
					dout=new DataOutputStream(s.getOutputStream());  
					dout.writeUTF("Connecting........"); 
					l2.setText("Connecting........");
					Thread.sleep(3000);
					DataInputStream dis=new DataInputStream(s.getInputStream());  
					String re=(String)dis.readUTF();	
					l3.setText(re);
					dout.writeUTF("Connected....");
					dout.flush();
		}
		catch(Exception w){}	
		
	}
	public void actionPerformed(ActionEvent e)
	{
		
		if(e.getSource()==b1)
		{
			try
			{
					s=new Socket("localhost",6666); 
					dout=new DataOutputStream(s.getOutputStream());  
					dout.writeUTF(t1.getText());  
					dout.flush(); 
					if(t1.getText().startsWith("www"))
					{
					address=InetAddress.getByName(t1.getText());
					hostid=address.getHostAddress();
					hostn=address.getHostName();
					l2.setText("Replied by Server Tracked IP: "+hostid);
					l3.setText("Domain name: "+hostn);
					b2.setBounds(150,250,400,30);
					b2.setText("<HTML>Click the <FONT color=\"#000099\"><U>"+hostn+"</U></FONT>"+ " to go to the website.</HTML>");
					}
					else
					{
						t1.setText("Invalid URL.....");
					}
			}		
			catch(Exception we)
			{
				t1.setText("Exception no Internet connections.");
			}
		}		
		else if(e.getSource()==b2)
		{
						
						Desktop desktop = Desktop.getDesktop();
						try 
						{
							desktop.browse(new URI("http://"+hostid));
						} 
						catch (IOException | URISyntaxException ep)
						{
						// TO DO Auto-generated catch block
							t1.setText("error");
						// ep.printStackTrace();
						}
			
		}
	}
	public static void main(String sde[])
	{
		new eclient();
	}
}
