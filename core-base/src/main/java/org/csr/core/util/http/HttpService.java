package org.csr.core.util.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.csr.core.util.ObjUtil;
import org.csr.core.util.io.IOUtil;

public class HttpService {

	public String httpPostCallBack(String host, SetHttp<HttpPost> setHttp) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpResponse response;
		try {

			// 创建一个本地Cookie存储的实例
			CookieStore cookieStore = new BasicCookieStore();
			// 创建一个本地上下文信息
			HttpContext localContext = new BasicHttpContext();
			// 在本地上下问中绑定一个本地存储
			localContext.setAttribute(HttpClientContext.COOKIE_STORE,cookieStore);
			HttpPost httppost = new HttpPost(new URI(host));
			setHttp.addHeader(httppost);
			List<NameValuePair> param = setHttp.getParam();
			
			if(ObjUtil.isNotEmpty(param)){
				httppost.setEntity(new UrlEncodedFormEntity(param,setHttp.getUnicode()));
			}
			
			response = httpclient.execute(httppost, localContext);
			HttpEntity entity = response.getEntity();
			setHttp.getCookie(cookieStore);
			return EntityUtils.toString(entity);
		} catch (Exception e) {
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
			}
		}
		return "";
	}

	
	public String httpPutCallBack(String host, SetHttp<HttpPut> setHttp) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpResponse response;
		try {

			// 创建一个本地Cookie存储的实例
			CookieStore cookieStore = new BasicCookieStore();
			// 创建一个本地上下文信息
			HttpContext localContext = new BasicHttpContext();
			// 在本地上下问中绑定一个本地存储
			localContext.setAttribute(HttpClientContext.COOKIE_STORE,cookieStore);
			HttpPut httpPut = new HttpPut(new URI(host));
			setHttp.addHeader(httpPut);
			List<NameValuePair> param = setHttp.getParam();
			if(ObjUtil.isNotEmpty(param)){
				httpPut.setEntity(new UrlEncodedFormEntity(param,setHttp.getUnicode()));
			}
			response = httpclient.execute(httpPut, localContext);
			HttpEntity entity = response.getEntity();
			setHttp.getCookie(cookieStore);
			return EntityUtils.toString(entity);
		} catch (Exception e) {
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
			}
		}
		return "";
	}

	
	public String httpDeleteCallBack(String host, SetHttp<HttpDelete> setHttp) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpResponse response;
		try {

			// 创建一个本地Cookie存储的实例
			CookieStore cookieStore = new BasicCookieStore();
			// 创建一个本地上下文信息
			HttpContext localContext = new BasicHttpContext();
			URIBuilder setPath = new URIBuilder().setPath(host);
			if(ObjUtil.isNotEmpty(setHttp.getParam())){
				setPath.setParameters(setHttp.getParam());
			}
			URI uri = setPath.build();
			// 在本地上下问中绑定一个本地存储
			localContext.setAttribute(HttpClientContext.COOKIE_STORE,cookieStore);
			HttpDelete httpDel = new HttpDelete(uri);
			setHttp.addHeader(httpDel);
			response = httpclient.execute(httpDel, localContext);
			HttpEntity entity = response.getEntity();
			setHttp.getCookie(cookieStore);
			return EntityUtils.toString(entity);
		} catch (Exception e) {
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
			}
		}
		return "";
	}

	
	public String httpGetCallBack(String host, SetHttp<HttpGet> setHttp) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpResponse response;
		try {
			// 创建一个本地Cookie存储的实例
			CookieStore cookieStore = new BasicCookieStore();
			// 创建一个本地上下文信息
			HttpContext localContext = new BasicHttpContext();
			// 在本地上下问中绑定一个本地存储
			localContext.setAttribute(HttpClientContext.COOKIE_STORE,cookieStore);
			URIBuilder setPath = new URIBuilder().setPath(host);
			if(ObjUtil.isNotEmpty(setHttp.getParam())){
				setPath.setParameters(setHttp.getParam());
			}
			URI uri = setPath.build();
			//
			HttpGet httpget = new HttpGet(uri);
			setHttp.addHeader(httpget);
			response = httpclient.execute(httpget, localContext);
			HttpEntity entity = response.getEntity();
			setHttp.getCookie(cookieStore);
			return EntityUtils.toString(entity);
		} catch (Exception e) {
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
			}
		}
		return "";
	}
	
	
	public String httpPostFileCallBack(String host, FileHttp<HttpPost> setHttp) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpResponse response;
		try {

			CookieStore cookieStore = new BasicCookieStore();
			HttpContext localContext = new BasicHttpContext();
			localContext.setAttribute(HttpClientContext.COOKIE_STORE,cookieStore);
			HttpPost httppost = new HttpPost(new URI(host));
			setHttp.addHeader(httppost);
			List<NameValuePair> param = setHttp.getParam();
			if(ObjUtil.isNotEmpty(param)){
				httppost.setEntity(new UrlEncodedFormEntity(param,setHttp.getUnicode()));
			}

			List<ContentParam> bodys = setHttp.getContentParam();
			if(ObjUtil.isNotEmpty(bodys)){
				MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
				for (ContentParam body : bodys) {
					MultipartEntityBuilder addPart = entityBuilder.addPart(body.getName(), body.getContentBody());
					if(ObjUtil.isNotEmpty(body.getCharset())){
						addPart.setCharset(body.getCharset());
					}
				}
				HttpEntity reqEntity = entityBuilder.build();
				httppost.setEntity(reqEntity);
			}

			response = httpclient.execute(httppost, localContext);
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
			}
		}
		return "";
	}
	
	public String httpPutFileCallBack(String host, FileHttp<HttpPut> setHttp) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpResponse response;
		try {

			CookieStore cookieStore = new BasicCookieStore();
			HttpContext localContext = new BasicHttpContext();
			localContext.setAttribute(HttpClientContext.COOKIE_STORE,cookieStore);
			HttpPut httppost = new HttpPut(new URI(host));
			setHttp.addHeader(httppost);
			List<NameValuePair> param = setHttp.getParam();
			if(ObjUtil.isNotEmpty(param)){
				httppost.setEntity(new UrlEncodedFormEntity(param,setHttp.getUnicode()));
			}

			List<ContentParam> bodys = setHttp.getContentParam();
			if(ObjUtil.isNotEmpty(bodys)){
				MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
				for (ContentParam body : bodys) {
					entityBuilder.addPart(body.getName(), body.getContentBody());
				}
				HttpEntity reqEntity = entityBuilder.build();
				httppost.setEntity(reqEntity);
			}

			response = httpclient.execute(httppost, localContext);
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
			}
		}
		return "";
	}
	
	public String httpPostStreamCallBack(String host, StreamHttp<HttpPost> setHttp) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpResponse response;
		try {

			CookieStore cookieStore = new BasicCookieStore();
			HttpContext localContext = new BasicHttpContext();
			localContext.setAttribute(HttpClientContext.COOKIE_STORE,cookieStore);
			HttpPost httppost = new HttpPost(new URI(host));
			setHttp.addHeader(httppost);
			List<NameValuePair> param = setHttp.getParam();
			if(ObjUtil.isNotEmpty(param)){
				httppost.setEntity(new UrlEncodedFormEntity(param,setHttp.getUnicode()));
			}

			httppost.setEntity(new StringEntity(setHttp.getStream(),setHttp.getUnicode()));
			response = httpclient.execute(httppost, localContext);
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity, setHttp.getUnicode());
		} catch (Exception e) {
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
			}
		}
		return "";
	}
	public String httpPutStreamCallBack(String host, StreamHttp<HttpPut> setHttp) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpResponse response;
		try {

			CookieStore cookieStore = new BasicCookieStore();
			HttpContext localContext = new BasicHttpContext();
			localContext.setAttribute(HttpClientContext.COOKIE_STORE,cookieStore);
			HttpPut httppost = new HttpPut(new URI(host));
			setHttp.addHeader(httppost);
			List<NameValuePair> param = setHttp.getParam();
			if(ObjUtil.isNotEmpty(param)){
				httppost.setEntity(new UrlEncodedFormEntity(param,setHttp.getUnicode()));
			}

			httppost.setEntity(new StringEntity(setHttp.getStream(),setHttp.getUnicode()));
			response = httpclient.execute(httppost, localContext);
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity, setHttp.getUnicode());
		} catch (Exception e) {
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
			}
		}
		return "";
	}
	
	/**
	 * 下载文件，到内存里
	 * @param host
	 * @param setHttp
	 * @return
	 */
	public byte[] downLoadFile(String host, DownLoadHttp setHttp) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpResponse response;
		try {
			// 创建一个本地Cookie存储的实例
			CookieStore cookieStore = new BasicCookieStore();
			// 创建一个本地上下文信息
			HttpContext localContext = new BasicHttpContext();
			// 在本地上下问中绑定一个本地存储
			localContext.setAttribute(HttpClientContext.COOKIE_STORE,cookieStore);
			URIBuilder setPath = new URIBuilder().setPath(host);
			if(ObjUtil.isNotEmpty(setHttp.getParam())){
				setPath.setParameters(setHttp.getParam());
			}
			//
			HttpRequestBase httpget = setHttp.getHttp();
			httpget.setURI(setPath.build());
			setHttp.addHeader(httpget);
			response = httpclient.execute(httpget, localContext);
			HttpEntity entity = response.getEntity();
			setHttp.getCookie(cookieStore);
			if(entity.getContentLength()>0){
				return IOUtil.bytesFromStream(entity.getContent());
			}
		} catch (Exception e) {
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
			}
		}
		return new byte[0];
    }
	
	
	/**
	 * 下载文件，文件流里
	 * @param host
	 * @param setHttp
	 * @return
	 */
	public void downLoadOutputStream(String host,OutputStream outos, DownLoadHttp setHttp) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpResponse response;
		try {
			// 创建一个本地Cookie存储的实例
			CookieStore cookieStore = new BasicCookieStore();
			// 创建一个本地上下文信息
			HttpContext localContext = new BasicHttpContext();
			// 在本地上下问中绑定一个本地存储
			localContext.setAttribute(HttpClientContext.COOKIE_STORE,cookieStore);
			URIBuilder setPath = new URIBuilder().setPath(host);
			if(ObjUtil.isNotEmpty(setHttp.getParam())){
				setPath.setParameters(setHttp.getParam());
			}
			//
			HttpRequestBase httpget = setHttp.getHttp();
			setHttp.addHeader(httpget);
			httpget.setURI(setPath.build());
			response = httpclient.execute(httpget, localContext);
			HttpEntity entity = response.getEntity();
			setHttp.getCookie(cookieStore);
			if(entity.getContentLength()>0){
				IOUtil.pipe(entity.getContent(), outos);
			}
		} catch (Exception e) {
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
			}
		}
    }
	
	// file1与file2在同一个文件夹下 filepath是该文件夹指定的路径
	public void SubmitPost(String url, String filename1, String filename2, String filepath) {
	}

	public static void main(String[] args) throws URISyntaxException {
		URI uri = new URI("http://127.0.0.1");
		System.out.println(uri.toString());
	}
}
