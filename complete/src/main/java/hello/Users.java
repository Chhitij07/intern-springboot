package hello;

import java.util.HashMap;
import java.util.Map;

public class Users {
	
	public static Map<String,String> users=new HashMap<>();
	
	public void add(String user,String sessionID)
	{
		users.put(user, sessionID);
	}

	public String get(String user)
	{
		return users.get(user);
	}
	public void show()
	{
		System.out.println(users);
	}
}