package pcclient.networking;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class RabbitMQBroker 
{
	
	public RabbitMQBroker(String RMQDir, String nodeName, int port)
	{
		
		String arguments[] = {"RABBITMQ_NODE_PORT=" +port};
		ProcessBuilder pb = new ProcessBuilder(new String[]{"cmd","/c",  "start",  "rabbitmq-server", arguments[0]});
		Map<String, String> environ = pb.environment();
		environ.put("ERLANG_HOME", System.getenv("ERLANG_HOME"));
		environ.put("RABBITMQ_NODE_PORT", Integer.toString(port));
		//environ.put("RABBITMQ_NODENAME", nodeName);
		
		Process p;
		try {
			pb.directory(new File(RMQDir));
			p = pb.start();
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	public static void main(String args[])
	{
		new RabbitMQBroker("C:\\Program Files\\RabbitMQ Server\\rabbitmq_server-3.0.4\\sbin","rmqserver", 8080);
	}
	
}
