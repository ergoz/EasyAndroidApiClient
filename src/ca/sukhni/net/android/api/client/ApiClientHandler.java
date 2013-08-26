package ca.sukhni.net.android.api.client;

/**
 * interface to Api client response handler
 * @author malsukhni
 *
 */
public interface ApiClientHandler
{
	/**
	 * this event triggered when the response code in the range [100,199]
	 * @param status response status code
	 * @param responseStatus response status line
	 * @param entity the response entity, SEE {@link ResponseEntity}
	 */
	public abstract void onInformational(Status status,String responseStatus,ResponseEntity entity);
	/**
	 * this event triggered when the response code in the range [200,299]
	 * @param status response status code
	 * @param responseStatus response status line
	 * @param entity the response entity, SEE {@link ResponseEntity}
	 */
	public abstract void onSuccessful(Status status,String responseStatus,ResponseEntity entity);
	/**
	 * this event triggered when the response code in the range [300,399]
	 * @param status response status code
	 * @param responseStatus response status line
	 * @param entity the response entity, SEE {@link ResponseEntity}
	 */
	public abstract void onRedirection(Status status,String responseStatus,ResponseEntity entity);
	/**
	 * this event triggered when the response code in the range [400,499]
	 * @param status response status code
	 * @param responseStatus response status line
	 * @param entity the response entity, SEE {@link ResponseEntity}
	 */
	public abstract void onClientError(Status status,String responseStatus,ResponseEntity entity);
	/**
	 * this event triggered when the response code is >= 500
	 * @param status response status code
	 * @param responseStatus response status line
	 * @param entity the response entity, SEE {@link ResponseEntity}
	 */
	public abstract void onServerError(Status status,String responseStatus,ResponseEntity entity);
	/**
	 * this event triggered when an exception happens when executing the request
	 * @param exceptionStatus the exception status. SEE {@link ExceptionStatus}
	 * @param e
	 */
	public abstract void onException(ExceptionStatus exceptionStatus,Exception e);
}
