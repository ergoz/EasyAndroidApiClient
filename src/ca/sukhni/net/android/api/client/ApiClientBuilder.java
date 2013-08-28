package ca.sukhni.net.android.api.client;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.NameValuePair;

import ca.sukhni.net.android.logger.Logger;

interface BuilderInterface
{
	/**
	 * set the http request method, See {@link Method}
	 * @param method to set
	 * @return
	 */
	public abstract ApiClientBuilder setMethod(Method method);
	/**
	 * set base uri. 
	 * <p>i.e. http://www.yoursite.com/path/path</P>
	 * @return
	 */
	public abstract ApiClientBuilder setBaseUri(String baseUri);
	/**
	 * set the connection timeout
	 * @param connectionTimeout the connectionTimeout to set
	 * @return
	 */
	public abstract ApiClientBuilder setConnectionTimeout(int connectionTimeout);
	/**
	 * set the socket timeout
	 * @param socketTimeout the socketTimeout to set
	 * @return
	 */
	public abstract ApiClientBuilder setSocketTimeout(int socketTimeout);
	/**
	 * set connection timeout retry count, default is zero
	 * @param connectionTimeoutRetry the connectionTimeoutRetry to set
	 * @return
	 */
	public abstract ApiClientBuilder setConnectionTimeoutRetry(int connectionTimeoutRetry);
	/**
	 * set socket timeout retry count, default is zero
	 * @param socketTimeoutRetry the socketTimeoutRetry to set
	 * @return
	 */
	public abstract ApiClientBuilder setSocketTimeoutRetry(int socketTimeoutRetry);
	/**
	 * set the connection timeout retry flag
	 * @param enableConntectionTimeoutRetry the enableConntectionTimeoutRetry to set
	 * @return
	 */
	public abstract ApiClientBuilder setEnableConntectionTimeoutRetry(boolean enableConntectionTimeoutRetry);
	/**
	 * set the socket timeout retry flag
	 * @param enableSocketTimeoutRetry the enableSocketTimeoutRetry to set
	 * @return
	 */
	public abstract ApiClientBuilder setEnableSocketTimeoutRetry(boolean enableSocketTimeoutRetry);
	/**
	 * add path to the request uri, the path can consist of one or more elements
	 * i.e. addPath("route"), addPath("route/to/home")
	 * null or empty paths will not be added
	 * @param path to add
	 * @return
	 */
	public abstract ApiClientBuilder addPath(String path);
	/**
	 * add paths to the request uri, the path can consist of one or more elements
	 * i.e. addPaths("route","to",home"), addPaths("route/to/","home")
	 * null or empty paths will not be added
	 * @param paths to be added
	 * @return
	 */
	public abstract ApiClientBuilder addPaths(String paths);
	/**
	 * add parameter to the request
	 * @param name the parameter name
	 * @param value the parameter value
	 * @return
	 */
	public abstract ApiClientBuilder addParam(String name, String value);
	/**
	 * add header to the request
	 * @param name the header name
	 * @param value the header value
	 * @return
	 */
	public abstract ApiClientBuilder addHeader(String name, String value);
	/**
	 * add text to the request content
	 * @param name the content name
	 * @param value the content value
	 * @return
	 */
	public abstract ApiClientBuilder setTextContent(String value);
	/**
	 * build the api call
	 * @return
	 */
	public ApiClient build();
	
}
public class ApiClientBuilder implements BuilderInterface
{
	private ApiClient apiClient;

	public ApiClientBuilder()
	{
		apiClient = new ApiClient();
	}
	@Override
	public ApiClientBuilder setMethod(Method method)
	{
		apiClient.setMethod(method);
		return this;
	}

	@Override
	public ApiClientBuilder setBaseUri(String baseUri)
	{
		apiClient.setBaseUri(baseUri);
		return this;
	}

	@Override
	public ApiClientBuilder addParam(String name, String value)
	{
		apiClient.addParam(name, value);
		return this;
	}

	@Override
	public ApiClientBuilder addHeader(String name, String value)
	{
		apiClient.addHeader(name, value);
		return this;
	}

	@Override
	public ApiClientBuilder setTextContent(String value)
	{
		apiClient.setTextContent(value);
		return this;
	}
	@Override
	public ApiClient build()
	{
		return apiClient;
	}
	@Override
	public ApiClientBuilder setConnectionTimeout(int connectionTimeout)
	{
		apiClient.setConnectionTimeout(connectionTimeout);
		return this;
	}
	@Override
	public ApiClientBuilder setSocketTimeout(int socketTimeout)
	{
		apiClient.setSocketTimeout(socketTimeout);
		return this;
	}
	@Override
	public ApiClientBuilder setConnectionTimeoutRetry(int connectionTimeoutRetry)
	{
		apiClient.setConnectionTimeoutRetry(connectionTimeoutRetry);
		return this;
	}
	@Override
	public ApiClientBuilder setSocketTimeoutRetry(int socketTimeoutRetry)
	{
		apiClient.setSocketTimeoutRetry(socketTimeoutRetry);
		return this;
	}
	@Override
	public ApiClientBuilder setEnableConntectionTimeoutRetry(boolean enableConntectionTimeoutRetry)
	{
		apiClient.setEnableConntectionTimeoutRetry(enableConntectionTimeoutRetry);
		return this;
	}
	@Override
	public ApiClientBuilder setEnableSocketTimeoutRetry(boolean enableSocketTimeoutRetry)
	{
		apiClient.setEnableSocketTimeoutRetry(enableSocketTimeoutRetry);
		return this;
	}
	@Override
	public ApiClientBuilder addPath(String path)
	{
		apiClient.addPath(path);
		return this;
	}
	@Override
	public ApiClientBuilder addPaths(String paths)
	{
		apiClient.addPaths(paths);
		return this;
	}
	
	@Override
	public String toString()
	{
		String combine = "Base Uri: " + apiClient.getBaseUri() + " \n"
				+ "Paths: " + strPaths() + " \n"
				+ "Params: " + strParameters() + "\n";
		return combine;
	}
	
	private String strParameters()
	{
		// add parameters
		String combinedParams = "";
		if (!apiClient.getParams().isEmpty())
		{
			combinedParams += "?";
			for (NameValuePair p : apiClient.getParams())
			{
				String paramString;
				try
				{
					paramString = p.getName() + "=" + URLEncoder.encode(p.getValue(), apiClient.getCharSetType());
					if (combinedParams.length() > 1)
					{
						combinedParams += "&" + paramString;
					}
					else
					{
						combinedParams += paramString;
					}
				}
				catch (UnsupportedEncodingException e)
				{
					Logger.printStackTrace(e);
					return "";
				}
			}
			return combinedParams;
		}
		return "";
	}
	private String strPaths()
	{
		if(apiClient.getPath()!=null)
		{
			StringBuilder pathBuilder = new StringBuilder();
			int size = apiClient.getPath().size();
			for(int i=0;i<size;i++)
			{
				String path = apiClient.getPath().get(i).replace("/", "").replace("\\", "");
				if(i<size) pathBuilder.append("/");
				pathBuilder.append(path);
			}
			return pathBuilder.toString();
		}
		else
		{
			return "";
		}
	}
	
}
