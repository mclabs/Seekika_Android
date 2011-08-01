package com.seekika.android.app.helpers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import android.util.Log;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class S3 {
	
	private static AmazonS3 s3 = null;
	private static BasicAWSCredentials credentials=null;
	private static final String AWS_ACCESSKEY="1D5RC1VT1EJJ94CR70G2";
	private static final String AWS_SECRET_KEY="cGKWNRA6jo4BbFGioPYTN8/EwXf6gGdTWg5uBLrP";
		
	
	public static AmazonS3 getInstance() {
		if ( s3 == null ) {
			credentials=new BasicAWSCredentials(AWS_ACCESSKEY,AWS_SECRET_KEY);
			s3=new AmazonS3Client(credentials);
		}
		return s3;
	}
	
	public static void createBucket( String bucketName ) {
		getInstance().createBucket( bucketName );
	}		
	
	public static void deleteBucket( String bucketName ) {
		getInstance().deleteBucket(  bucketName );
	}
	
	


	public static void createObjectForBucket( String bucketName, String objectName, String data ) {
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream( data.getBytes() );
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength( data.getBytes().length );
			getInstance().putObject(bucketName, objectName, bais, metadata );
		}
		catch ( Exception exception ) {
			Log.e( "TODO", "createObjectForBucket" );
		}
	}
	
	public static void putObjectInBucket(String bucketName,String fileName,File file){
		try{
			PutObjectRequest putObjectRequest=new PutObjectRequest(bucketName,fileName,file);
			ObjectMetadata metaData = new ObjectMetadata();	
			//metaData.setContentType("audio/mp3"); //binary data
			//putObjectRequest.setMetadata(metaData);			
			putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
			//s3.putObject(putObject);
			getInstance().putObject(putObjectRequest);
		}catch(Exception exception){
			Log.e("TODO","");
		}
		
		
	}
	
	protected static String read( InputStream stream ) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream( 8196 );
			byte[] buffer = new byte[1024];
			int length = 0;
			while ( ( length = stream.read( buffer ) ) > 0 ) {
				baos.write( buffer, 0, length );
			}
			
			return baos.toString();
		}
		catch ( Exception exception ) {
			return exception.getMessage();
		}
	}
	




}
