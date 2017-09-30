import java.awt.*;
import java.net.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
public class eserver extends JFrame implements Runnable , ActionListener
{
	JButton b1,b2,b3,b4,b5;
	JLabel l1,l2,l3;
	JLabel l4,l5;
	JLabel lt[]=new JLabel[9];
	JButton bt[]=new JButton[14];
	ServerSocket ss;
	Socket s;
	Thread td;
	int k=1,i=0,j=0,n=0;
	InetAddress address;
	String hostid,hostn;
	Connection con;
	eserver()
	{
		//l1=new JLabel("");
		super("server...");
		//InetAddress address=InetAddress.getByName(null);
		getContentPane().setBackground(Color.pink);
		l1=new JLabel("");
		l2=new JLabel("");
		l3=new JLabel("");
		l4=new JLabel("");
		l5=new JLabel("Recent search by client:");
		add(l5);
		l5.setBounds(500,120,300,40);
		for(int x=0;x<8;x++)
		{
			lt[x]=new JLabel("");
			add(lt[x]);
			lt[x].setForeground(Color.blue);
			lt[x].setFont (new Font ("Times New Roman", Font.BOLD,18));
			lt[x].setBounds(50,270,400,40+n);
			n=n+40;
		}
		add(l1);
		add(l2);
		add(l3);
		add(l4);
		
		l2.setForeground(Color.blue);
		  l3.setForeground(Color.blue);
		   l4.setForeground(Color.blue);
		   l5.setForeground(Color.red);
		   l1.setFont (new Font ("Times New Roman", Font.BOLD,20));
		   l2.setFont (new Font ("Times New Roman", Font.BOLD,18));
		   l3.setFont (new Font ("Times New Roman", Font.BOLD,20));
		   l4.setFont (new Font ("Times New Roman", Font.BOLD,20));
		    l5.setFont (new Font ("Times New Roman", Font.BOLD,20));
		   
		  
		
		l1.setBounds(50,100,200,30);
		l2.setBounds(50,140,400,30);
		l3.setBounds(50,180,400,30);
		l4.setBounds(50,220,400,30);
		for(int i=0;i<12;i++)
		{
			bt[i]=new JButton(" ");
			add(bt[i]);
			bt[i].setForeground(Color.blue);	
			bt[i].addActionListener(this);
		}
		int j=0,i=0;
				try
				{
					Class.forName("oracle.jdbc.driver.OracleDriver");  
					con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","system");
					Statement stmts=con.createStatement();  
					ResultSet rs=stmts.executeQuery("select * from address2 order by id desc");  
						while(rs.next()&&i!=11)
						{
							j=j+40;
							//rs.next();   
							bt[i].setLabel(rs.getString(2));
							bt[i].setBounds(500,140+j,260,40);
							i++;
						}
				}
				catch(Exception e){}	
		setVisible(true);
		setSize(800,700);
		setLayout(null);
		
		try
			{
				ss=new ServerSocket(6666); 
				while(true)
				{ 
				s=ss.accept();
				td=new Thread(this);
				td.start();
				}
		//System.out.println("message= "+str);  
			}
		catch(Exception ex)
		{}
		
	}	
	public void actionPerformed(ActionEvent ev)
	{
		for(i=0;i<12;i++)
		{
			try
			{
				String ipad=ev.getActionCommand();
				if(ev.getSource()==bt[i])
				{
					 Desktop desk = Desktop.getDesktop();
					 desk.browse(new URI("http://"+ipad));
				}
			}
			catch(Exception xx)
			{
				
			}
		}
		
	}
	public void run()
	{
		try
		{
			if(k==1)
			{
				DataInputStream dis=new DataInputStream(s.getInputStream());  
			String re=(String)dis.readUTF();
			l1.setText(re);
			DataOutputStream dos=new DataOutputStream(s.getOutputStream());
			dos.writeUTF("Connected....");
			String r2=(String)dis.readUTF();
			l2.setText(r2); 
			k=0;
			}
			DataInputStream dis=new DataInputStream(s.getInputStream());  
			String re=(String)dis.readUTF();
			if(re.startsWith("www"))
			{
				l1.setText("Domain: ");
				l2.setText("Requested by client: "+re); 
					address=InetAddress.getByName(re);
					hostid=address.getHostAddress();
					hostn=address.getHostName();
					l3.setText("Tracked IP: "+hostid);
					l4.setText("Domain name: "+hostn);
					
					
					URL ipapi = new URL("https://ipapi.co/"+hostid+"/json/");
							URLConnection c = ipapi.openConnection();
							c.setRequestProperty("User-Agent", "java-ipapi-client");
							BufferedReader reader = new BufferedReader(new InputStreamReader(c.getInputStream()) );
							String location = reader.readLine();
							reader.close();
							String loc=location.replace('{',' ').replace('}',' ');
							String prt[]=loc.split(",");
							for(int x=0;x<8;x++)
							{
								lt[x].setText(prt[x]);
							}
					
					PreparedStatement stmt=con.prepareStatement("insert into address2 (ip) values(?)");  
					stmt.setString(1,hostid);  
					stmt.executeUpdate();
					Statement stmts=con.createStatement();  
					ResultSet rs=stmts.executeQuery("select * from address2 order by id desc");  
					i=0;
						while(rs.next()&&i!=12)
						{
							//j=j+40;
							//rs.next();   
							bt[i].setLabel(rs.getString(2));
							//bt[i].setBounds(500,140+j,260,40);
							i++;
						}
						
							
						
			}	
			else if(!re.startsWith("www"))
			{
				l2.setText("Invalid URL......"); 
			}
		}
		catch(Exception e)
		{
			l1.setText(String.valueOf(e));
		}
			
	}	
		
	
	public static void main(String sde[])
	{
		new eserver();
	}
}
