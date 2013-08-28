/**
 	Android API Client: A simple and easy way to make an API call for android developers
    Copyright (C) 2013  Moh. Alsukhni

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 **/
package ca.sukhni.net.android.api.client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.NoRouteToHostException;
import java.net.PortUnreachableException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.channels.UnresolvedAddressException;
import java.util.ArrayList;

import org.apache.http.ConnectionClosedException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import ca.sukhni.net.android.api.socket.HttpUtils;
import ca.sukhni.net.android.logger.Logger;


abstract class RestClient extends BaseClient
{
	public final static String							TAG							= RestClient.class.getSimpleName();
	
	protected ArrayList<NameValuePair> 					mParams						= null;
    protected ArrayList<NameValuePair> 					mHeaders					= null;
    protected String 									mContent					= null;
    protected ArrayList<String>							mPath						= null;
    protected Method									mMethod						= null;
    private String 										fullUrl						= null;
    protected String									mBaseUri					= null;
    protected Integer									mResponseCode				= null;
    protected String									mReponseStatusLine			= null;
    protected String									mCharSetType				= null;
    protected String									mContentType				= null;
    protected String									mExceptionMessage			= null;
    protected ResponseEntity							mResponseEntity				= null;
    protected long										mReponseContentLength		= -1;
    
    /**
     * Rest client constructor: the constructor will an empty parameters, headers, content, and paths list.
     * the default http method -if not set- is GET
     * the default char set type -if not set- is (UTF-8)
     * the default content type is - if not set- (application/xml)
     */
    public RestClient()
    {
    	mMethod = Method.GET;
    	mParams = new ArrayList<NameValuePair>();
        mHeaders = new ArrayList<NameValuePair>();
        mPath = new ArrayList<String>();
        
        mCharSetType = "UTF-8";
        mContentType = MediaType.APPLICATION_XML;
    }
    /**
     * Get the http request method. See {@link Method}
     * i.e. Method.GET
     * @return method 
     */
	public Method getMethod()
	{
		return mMethod;
	}
	/**
	 * set the http request method, See {@link Method}
	 * @param method to set
	 */
	public void setMethod(Method method)
	{
		this.mMethod = method;
	}
	/**
	 * get base Uri of the request.
	 * @return string value of the Uri
	 */
	public String getBaseUri()
	{
		return mBaseUri;
	}
	/**
	 * set base uri. 
	 * <p>i.e. http://www.yoursite.com/path/path</P>
	 * @param baseUri
	 */
	public void setBaseUri(String baseUri)
	{
		this.mBaseUri = baseUri;
	}

	/**
	 * @return the params
	 */
	protected ArrayList<NameValuePair> getParams()
	{
		return mParams;
	}
	/**
	 * @return the headers
	 */
	protected ArrayList<NameValuePair> getHeaders()
	{
		return mHeaders;
	}
	/**
	 * @return the paths
	 */
	protected ArrayList<String> getPath()
	{
		return mPath;
	}
	
	/**
	 * @return the charSetType
	 */
	protected String getCharSetType()
	{
		return mCharSetType;
	}
	/**
	 * @param CharSetType the charSetType to set
	 */
	protected void setmCharSetType(String charSetType)
	{
		this.mCharSetType = charSetType;
	}
	
	@Override
	protected void addPath(String path)
	{
		if(path!=null && path.trim().length()!=0)
		{
			mPath.add(path.trim());
		}
	}

	@Override
	protected void addPaths(String... paths)
	{
		if(paths!=null)
		{
			for(int i=0;i<paths.length;i++)
			{
				addPath(paths[i]);
			}
		}
	}

	@Override
	protected void addParam(String name, String value)
	{
		value = (value != null) ? value : "";
		mParams.add(new BasicNameValuePair(name, value));
		Logger.debug("RestClient.AddParam(\"" + name + "\",\"" + value + "\")");
	}

	@Override
	protected void addHeader(String name, String value)
	{
		value = (value != null) ? value : "";
		mHeaders.add(new BasicNameValuePair(name, value));
		Logger.debug("RestClient.AddHeader(\"" + name + "\",\"" + value + "\")");
	}

	@Override
	protected void setTextContent(String value)
	{
		mContent = value;
        Logger.debug("RestClient.setTextContent("+ value + ")");
	}
	
	@Override
	protected void execute() throws UnsupportedEncodingException,UnresolvedAddressException, NoRouteToHostException, ConnectTimeoutException, SocketTimeoutException, UnknownHostException, ConnectionClosedException, FileNotFoundException, IOException, Exception
	{
		execute(mMethod);
	}
	
	@Override
	protected void execute(Method method) throws UnsupportedEncodingException,UnresolvedAddressException, NoRouteToHostException, ConnectTimeoutException, SocketTimeoutException, UnknownHostException, ConnectionClosedException, FileNotFoundException, IOException, Exception
	{
		// add paths
		StringBuilder pathBuilder = new StringBuilder();
		if(mPath!=null)
		{
			int size = mPath.size();
			for(int i=0;i<size;i++)
			{
				String path = mPath.get(i).replace("/", "").replace("\\", "");
				if(i<size) pathBuilder.append("/");
				pathBuilder.append(path);
			}
		}
		
		// add parameters
		String combinedParams = "";
		if (!mParams.isEmpty())
		{
			combinedParams += "?";
			for (NameValuePair p : mParams)
			{
				String paramString = p.getName() + "=" + URLEncoder.encode(p.getValue(), mCharSetType);

				if (combinedParams.length() > 1)
				{
					combinedParams += "&" + paramString;
				}
				else
				{
					combinedParams += paramString;
				}
			}
		}

		switch (method)
		{
			case GET:
			{
				fullUrl = mBaseUri + pathBuilder.toString() + combinedParams;
				Logger.debug("RestClient GET: " + fullUrl);
				HttpGet request = new HttpGet(fullUrl);
				// add headers
				for (NameValuePair h : mHeaders)
				{
					request.addHeader(h.getName(), h.getValue());
				}
				executeRequest(request, mBaseUri);
				break;
			}
			case POST:
			{
				fullUrl = mBaseUri + pathBuilder.toString() + combinedParams;
				Logger.debug("RestClient POST: " + fullUrl);
				HttpPost request = new HttpPost(fullUrl);
				// add headers
				for (NameValuePair h : mHeaders)
				{
					request.addHeader(h.getName(), h.getValue());
				}
				if (mContent!=null)
				{
					StringEntity entity = new StringEntity(mContent, mCharSetType);
					entity.setContentEncoding(mCharSetType);
					entity.setContentType("application/xml");
					request.setEntity(entity);
				}
				executeRequest(request, fullUrl);
				break;
			}
			case PUT:
			{
				fullUrl = mBaseUri + pathBuilder.toString() + combinedParams;
				Logger.debug("RestClient PUT: " + fullUrl);
				HttpPut request = new HttpPut(mBaseUri + combinedParams);
				// add headers
				for (NameValuePair h : mHeaders)
				{
					request.addHeader(h.getName(), h.getValue());
				}
				
				if (mContent!=null)
				{
					StringEntity entity = new StringEntity(mContent, mCharSetType);
					entity.setContentEncoding(mCharSetType);
					entity.setContentType("application/xml");
					request.setEntity(entity);
				}
				executeRequest(request, fullUrl);
				break;
			}
			case DELETE:
			{
				fullUrl = mBaseUri + pathBuilder.toString() + combinedParams;
				Logger.debug("RestClient DELETE: " + fullUrl);
				HttpDelete request = new HttpDelete(mBaseUri + combinedParams);
				// add headers
				for (NameValuePair h : mHeaders)
				{
					request.addHeader(h.getName(), h.getValue());
				}
				executeRequest(request, fullUrl);
				break;
			}
		}
	}
	private void executeRequest(HttpUriRequest request, String url) throws UnresolvedAddressException,UnknownHostException,
	NoRouteToHostException,ConnectTimeoutException,SocketTimeoutException,ConnectionClosedException,FileNotFoundException,IOException,Exception
	{
		Logger.debug(TAG + ": executeRequest(HttpUriRequest request, String url)");
		HttpParams conParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(conParams, mConnectionTimeout);
        HttpConnectionParams.setSoTimeout(conParams, mSocketTimeout);
        HttpClient client = HttpUtils.getNewHttpClient(conParams);
        
        try
		{
			HttpResponse httpResponse = client.execute(request);
			mResponseCode = httpResponse.getStatusLine().getStatusCode();
			mReponseStatusLine = httpResponse.getStatusLine().getReasonPhrase();
			Logger.debug(TAG + ":executeRequest(HttpUriRequest request, String url): ResponseCode= " + mResponseCode + ", ReponseStatusLine= " + mReponseStatusLine);
			mResponseEntity = iniResponseEntity(httpResponse.getEntity());
			try
			{
				if(httpResponse.getEntity()!=null) mReponseContentLength = mResponseEntity.getContentLength();
			}
			catch(Exception ex){}
			
			
		}
        catch (ClientProtocolException e)
        {
        	Logger.error("E0001:RestClient:ClientProtocolException:executeRequest: " + url + "\n" + e.getMessage());
        	mExceptionMessage = "There was an error requesting information from the servers. [E0001]";
        	Logger.error(e.getCause().getLocalizedMessage());
        	//e.printStackTrace();
            client.getConnectionManager().shutdown();
            throw e;
        }
        catch (UnresolvedAddressException e)
        {
        	Logger.error("E0002:RestClient:UnresolvedAddressException:executeRequest: " + url + "\n" + e.getMessage());
        	mExceptionMessage = "There was an error resolving the server internet address. [E0002]";
        	client.getConnectionManager().shutdown();
        	throw e;
        }
        catch (UnknownHostException e)
        {
        	Logger.error("E0003:RestClient:UnknownHostException:executeRequest: " + url + "\n" + e.getMessage());
        	mExceptionMessage = "There was an error resolving the server internet address. [E0003]";
            client.getConnectionManager().shutdown();
            throw e;
        }
        catch(PortUnreachableException e)
        {
        	Logger.debug("E0004:RestClient:NoRouteToHostException:executeRequest: " + url + "\n" + e.getMessage());
        	mExceptionMessage = "There was an error finding a route to the server. An intermediate router, access point or gateway may be failing or a firewall is blocking the connection to the internet. [E0004]";
            client.getConnectionManager().shutdown();
            throw e;
        }
        catch (NoRouteToHostException e)
        {
        	Logger.debug("E0005:RestClient:NoRouteToHostException:executeRequest: " + url + "\n" + e.getMessage());
        	mExceptionMessage = "There was an error finding a route to the server. An intermediate router, access point or gateway may be failing or a firewall is blocking the connection to the internet. [E0004]";
            client.getConnectionManager().shutdown();
            throw e;
        }
        catch (ConnectTimeoutException e)
        {
        	Logger.error("E0006:RestClient:ConnectTimeoutException:executeRequest: " + url + "\n" + e.getMessage());
        	Logger.debug("RestClient:ConnectTimeoutException:remain try count: " + mConnectionTimeoutRetry);
        	mExceptionMessage = "The attempt to connect to the server has timed out. The server is temporarily busy or otherwise congested at this time. [E0005]";
        	if(mConnectionTimeoutRetry>0 && mEnableConntectionTimeoutRetry==true)
        	{
        		mSocketTimeoutRetry--;
        		executeRequest(request,  url);
        		
        	}
        	else
        	{
        		client.getConnectionManager().shutdown();
        		throw e;
        	}
        }
        catch(SocketTimeoutException e)
        {
        	Logger.error("E0007:RestClient:SocketTimeoutException:executeRequest: " + url +"\n" + e.getMessage());
        	Logger.debug("RestClient:SocketTimeoutException:remain try count: " + mSocketTimeoutRetry);
        	mExceptionMessage = "The connection with the server has timed out. This could be due to a slow connection or poor signal strength. [E0006]";
        	if(mSocketTimeoutRetry>0 && mEnableSocketTimeoutRetry==true)
        	{
        		mSocketTimeoutRetry--;
        		executeRequest(request,  url);
        	}
        	else
        	{
        		client.getConnectionManager().shutdown();
        		throw e;
        	}
        }
        catch(ConnectionClosedException e)
        {
        	Logger.error("E0008:RestClient:ConnectionClosedException:executeRequest: " + url +"\n" + e.getMessage());
        	mExceptionMessage = "The internet connection as closed unexpectedly. This could be due to a slow connection or poor signal strength. [E0007]";
        	throw e;
        }
        catch(FileNotFoundException e)
        {
        	Logger.error("E0009:RestClient:FileNotFoundException:executeRequest: " + url +"\n" + e.getMessage());
        	mExceptionMessage = "The file requested was not found. [E0008]";
        	throw e;
        }
        catch (IOException e)
        {
        	Logger.error("E0010:RestClient:IOException:executeRequest: " + url + "\n" + e.getMessage());
        	mExceptionMessage = "There is a problem with internet connection, please check your internet connection. [E0009]";
            client.getConnectionManager().shutdown();
            throw e;
        }
        catch(Exception e)
        {
        	Logger.error("E0011:RestClient:Exception:executeRequest: " + url + "\n" + e.getMessage());
        	mExceptionMessage = "Unexpected error occurred ("+e.getMessage()+"). [E0010]";
            client.getConnectionManager().shutdown();
            throw e;
        }
	}

	@Override
	protected Integer getResponseCode()
	{
		return mResponseCode;
	}

	@Override
	protected String getResponseStatusLine()
	{
		return mReponseStatusLine;
	}

	@Override
	protected ResponseEntity getResponseEntity()
	{
		return mResponseEntity;
	}

	@Override
	protected String getResponseContentAsString() throws IllegalStateException, IOException
	{
		return mResponseEntity.getResponseContentAsString();
	}
	
	@Override
	protected long getResponseContentLength()
	{
		return mReponseContentLength;
	}
	
	/**
	 * 
	 * @param entity
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	private String readEntity(HttpEntity entity) throws IllegalStateException, IOException 
	{
		return HttpEntityHelper.readEntityAsString(entity);
	}
	
	private ResponseEntity iniResponseEntity(HttpEntity httpEntity)
	{
		ResponseEntityImpt responseEntity = new ResponseEntityImpt();
		if(httpEntity!=null)
		{
			responseEntity.setStreaming(httpEntity.isStreaming());
			responseEntity.setRepeatable(httpEntity.isRepeatable());
			responseEntity.setContentLength(httpEntity.getContentLength());
			responseEntity.setChuncked(httpEntity.isChunked());
			String strEntity = null;
			try
			{
				strEntity = readEntity(httpEntity);}
			catch(Exception ex)
			{
				Logger.printStackTrace(ex);
			}
			responseEntity.setStrEntity(strEntity);
			Header header = httpEntity.getContentType();
			if(header!=null)
			{
				responseEntity.getResponseHeaderImpt().setName(httpEntity.getContentType().getName());
				responseEntity.getResponseHeaderImpt().setValue(httpEntity.getContentType().getValue());
				responseEntity.getResponseHeaderImpt().setElements(httpEntity.getContentType().getElements());
			}
			else
			{
				responseEntity.setResponseHeaderImpt(null);
			}
			return responseEntity;
		}
		else 
		{
			 return null;
		}
	}
}
