package com.boscarine.finddup;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

public class FindDupsClient {
	protected final Log logger = LogFactory.getLog(getClass());

	protected final WebResource serverResource;

	/**
	 * @param serverURL
	 *            http://${host}:${port}/${context}
	 */
	public FindDupsClient(String serverURL) {
		Client client = new Client();
		serverResource = client.resource(serverURL);
	}

	// wrapper for exceptions...need to give more meaningful 404
	<E> E get(String path, Class<E> returnType, Map<String, String> params) {
		return get(path, returnType, params, false);
	} // wrapper for exceptions...need to give more meaningful 404

	<E> E get(String path, Class<E> returnType, Map<String, String> params, boolean suppressError) {
		// Conventional Jersey client calls.
		// broken into 2 calls so error can display full URL below.
		WebResource serviceResource = serverResource.path(path);
		for (String key : params.keySet()) {
			serviceResource = serviceResource.queryParam(key, params.get(key));
		}
		try {
			return serviceResource.get(returnType);
		} catch (UniformInterfaceException up) {
			logger.error("error getting " + serviceResource.getURI().toString());
			if (suppressError) {
				return null;
			}
			throw up;
		}
	}

	<E> E get(String path, Class<E> returnType) {
		return get(path, returnType, new HashMap<String, String>());
	}

	<E> E get(String path, Class<E> returnType, boolean suppressExceptions) {
		return get(path, returnType, new HashMap<String, String>(), suppressExceptions);
	}
	// wrapper for exceptions...need to give more meaningful 404
	String post(String path, Object input) {
		// Conventional Jersey client calls.
		// broken into 2 calls so error can display full URL below.
		final WebResource serviceResource = serverResource.path(path);

		try {
			return serviceResource.post(String.class, input);
		} catch (UniformInterfaceException up) {
			logger.error("error posting " + serviceResource.getURI().toString());
			throw up;
		}
	}
}
