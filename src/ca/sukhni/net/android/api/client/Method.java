package ca.sukhni.net.android.api.client;

public enum Method
{
    GET, PUT, POST, DELETE;
    
    public static Method fromMethod(String method)
    {
    	for(Method m: Method.values())
    	{
    		if(m.name().equalsIgnoreCase(method)) return m;
    	}
    	return null;
    }
}