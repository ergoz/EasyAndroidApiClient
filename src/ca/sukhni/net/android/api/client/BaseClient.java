package ca.sukhni.net.android.api.client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.channels.UnresolvedAddressException;
import org.apache.http.ConnectionClosedException;
import org.apache.http.conn.ConnectTimeoutException;

public abstract class BaseClient
{
	public static String		TAG									= BaseClient.class.getSimpleName();
	protected int  				mConnectionTimeout					= 2000;
	protected int 				mSocketTimeout						= 2000;
	protected boolean			mEnableConntectionTimeoutRetry		= true;
	protected boolean			mEnableSocketTimeoutRetry			= true;
	protected int				mConnectionTimeoutRetry				= 0;
	protected int				mSocketTimeoutRetry					= 0;
	/**
	 * add parameter to the request
	 * @param name the parameter name
	 * @param value the parameter value
	 */
	protected abstract void addParam(String name,String value);
	/**
	 * add header to the request
	 * @param name the header name
	 * @param value the header value
	 */
	protected abstract void addHeader(String name, String value);
	/**
	 * add text to the request content
	 * @param name the content name
	 * @param value the content value
	 */
	protected abstract void addTextContent(String name, String value);
	/**
	 * add path to the request uri, the path can consist of one or more elements
	 * i.e. addPath("route"), addPath("route/to/home")
	 * null or empty paths will not be added
	 * @param path to add
	 */
	protected abstract void addPath(String path);
	/**
	 * add paths to the request uri, the path can consist of one or more elements
	 * i.e. addPaths("route","to",home"), addPaths("route/to/","home")
	 * null or empty paths will not be added
	 * @param paths to be added
	 */
	protected abstract void addPaths(String... paths);
	/**
	 * execute the request
	 * @param method the request method, SEE {@link Method}
	 * @throws UnsupportedEncodingException
	 * @throws UnresolvedAddressException
	 * @throws UnknownHostException
	 * @throws NoRouteToHostException
	 * @throws ConnectTimeoutException
	 * @throws SocketTimeoutException
	 * @throws ConnectionClosedException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws Exception
	 */
	protected abstract void execute(Method method) throws UnsupportedEncodingException,UnresolvedAddressException,UnknownHostException,
	NoRouteToHostException,ConnectTimeoutException,SocketTimeoutException,ConnectionClosedException,FileNotFoundException,IOException,Exception;
	/**
	 * execute the request
	 * @throws UnsupportedEncodingException
	 * @throws UnresolvedAddressException
	 * @throws UnknownHostException
	 * @throws NoRouteToHostException
	 * @throws ConnectTimeoutException
	 * @throws SocketTimeoutException
	 * @throws ConnectionClosedException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws Exception
	 */
	protected abstract void execute() throws UnsupportedEncodingException,UnresolvedAddressException,UnknownHostException,
	NoRouteToHostException,ConnectTimeoutException,SocketTimeoutException,ConnectionClosedException,FileNotFoundException,IOException,Exception;
	/**
	 * get the response code
	 * @return integer value of the response response code
	 */
	protected abstract Integer getResponseCode();
	/**
	 * get the response status line
	 * @return string value of the status line
	 */
	protected abstract String getResponseStatusLine();
	/**
	 * get the response entity. SEE {@link ResponseEntity}
	 * @return
	 */
	protected abstract ResponseEntity getResponseEntity();
	/**
	 * get the response content as string
	 * @return string value of the response content
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	protected abstract String getResponseContentAsString() throws IllegalStateException, IOException;
	/**
	 * get the response content length
	 * if no response or null, then return -1
	 * @return
	 */
	protected abstract long getResponseContentLength();
	
	public BaseClient()
	{
		TAG = this.getClass().getSimpleName();
	}

	/**
	 * get connection timeout
	 * @return the connectionTimeout
	 */
	public int getConnectionTimeout()
	{
		return mConnectionTimeout;
	}

	/**
	 * set the connection timeout
	 * @param connectionTimeout the connectionTimeout to set
	 */
	public void setConnectionTimeout(int connectionTimeout)
	{
		this.mConnectionTimeout = connectionTimeout;
	}

	/**
	 * get socket timeout
	 * @return the mSocketTimeout
	 */
	public int getSocketTimeout()
	{
		return mSocketTimeout;
	}

	/**
	 * set the socket timeout
	 * @param socketTimeout the socketTimeout to set
	 */
	public void setSocketTimeout(int socketTimeout)
	{
		this.mSocketTimeout = socketTimeout;
	}

	/**
	 * get connection retry count, default is zero
	 * @return the connectionTimeoutRetry
	 */
	public int getConnectionTimeoutRetry()
	{
		return mConnectionTimeoutRetry;
	}

	/**
	 * set connection timeout retry count, default is zero
	 * @param connectionTimeoutRetry the connectionTimeoutRetry to set
	 */
	public void setConnectionTimeoutRetry(int connectionTimeoutRetry)
	{
		this.mConnectionTimeoutRetry = connectionTimeoutRetry;
	}

	/**
	 * get socket timeout retry count, default is zero
	 * @return the socketTimeoutRetry
	 */
	public int getSocketTimeoutRetry()
	{
		return mSocketTimeoutRetry;
	}

	/**
	 * set socket timeout retry count, default is zero
	 * @param socketTimeoutRetry the socketTimeoutRetry to set
	 */
	public void setSocketTimeoutRetry(int socketTimeoutRetry)
	{
		this.mSocketTimeoutRetry = socketTimeoutRetry;
	}
	
	/**
	 * check if the connection timeout retry is enabled
	 * @return the enableConntectionTimeoutRetry
	 */
	public boolean isConntectionTimeoutRetryEnabled()
	{
		return mEnableConntectionTimeoutRetry;
	}
	
	/**
	 * set the connection timeout retry flag
	 * @param enableConntectionTimeoutRetry the enableConntectionTimeoutRetry to set
	 */
	public void setEnableConntectionTimeoutRetry(boolean enableConntectionTimeoutRetry)
	{
		this.mEnableConntectionTimeoutRetry = enableConntectionTimeoutRetry;
	}
	
	/**
	 * check if the socket timeout retry is enabled
	 * @return the enableSocketTimeoutRetry
	 */
	public boolean isSocketTimeoutRetryenabled()
	{
		return mEnableSocketTimeoutRetry;
	}
	
	/**
	 * set the socket timeout retry flag
	 * @param enableSocketTimeoutRetry the enableSocketTimeoutRetry to set
	 */
	public void setEnableSocketTimeoutRetry(boolean enableSocketTimeoutRetry)
	{
		this.mEnableSocketTimeoutRetry = enableSocketTimeoutRetry;
	}
	
	
}
