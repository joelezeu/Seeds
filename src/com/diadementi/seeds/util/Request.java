package com.diadementi.seeds.util;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import android.os.AsyncTask;

import com.codecamp.libs.RestClient;
import com.codecamp.libs.RestClient.RequestMethod;

public class Request<D> extends AsyncTask<String,Void,Void>{
	D object;
	RequestMethod method;
	JsonParser<D> parser;
	mCallBack<D> myCallBack;
	Params p;
	protected SeedsException e=null;
	public Request(RestClient.RequestMethod method,JsonParser<D> j,mCallBack<D> c,Params p){
		this.method=method;
		this.parser=j;
		this.myCallBack=c;
		this.p=p;
	}
	public static interface mCallBack<E>{
		public abstract void  done(SeedsException e,E object);
	}
	public static interface Params{
	public abstract void params(RestClient client);
	}
	
	

	@Override
	protected Void doInBackground(String... params) {
		// TODO Auto-generated method stub
		try{
			RestClient client=new RestClient(params[0]);
			if(p!=null)
			p.params(client);
			client.Execute(method);
			SeedsResponse.checkResponse(client.getResponseCode(), client.getResponse());
			String response=client.getResponse();
			if(parser!=null&&response!=null)
			this.object=parser.parse(response);
		}catch(SeedsException ex){
			this.e=ex;
		}catch(ClientProtocolException ex){
			this.e=new SeedsException(SeedsResponse.CONNECT_TIMEOUT,ex);
		}catch(IOException ex){
			this.e=new SeedsException(SeedsResponse.CONNECT_TIMEOUT,ex);
		}
		return null;
	}
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		myCallBack.done(e, object);
	}

}
