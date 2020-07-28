package com.bkrmalick.covidtracker.configs;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamoDBConfig
{
	@Value("${amazon.access.key}")
	private String awsAccessKey;

	@Value("${amazon.access.secret-key}")
	private String awsSecretKey;

	@Value("${amazon.region}")
	private String awsRegion;

	@Value("${amazon.end-point.url}")
	private String awsDynamoDBEndPoint;

	/*uses client builder to return DB mapper*/
	@Bean
	public DynamoDBMapper mapper()
	{
		return new DynamoDBMapper(amazonDynamoDBConfig());
	}

	/*client builder*/
	public AmazonDynamoDB amazonDynamoDBConfig()
	{
		return AmazonDynamoDBClientBuilder.standard()
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(awsDynamoDBEndPoint,awsRegion))
				.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsAccessKey, awsSecretKey)))
				.build();
	}
}

