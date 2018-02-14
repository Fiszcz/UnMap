package functions;

public class Random {
	public static String randomString(int len)
	{
	    char[] str = new char[100];
	
	    for (int i = 0; i < len; i++)
	    {
	      str[i] = (char) (((int)(Math.random() * 26)) + (int)'A');
	    }
	
	    return (new String(str, 0, len));
	}
}