package com.bkrmalick.covidtracker.models.dynamo_db;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.io.Serializable;

@DynamoDBTable(tableName = "covidtracker.london-boroughs-population-density")
public class PopulationDensityRecord implements Serializable
{
	private String code,name,source;
	private int year;
	private double population,inlandAreaHectares,totalAreaHectares,populationPerHectare,
			squareKilometres,populationPerSquareKilometre;

	public PopulationDensityRecord()
	{

	}
	public PopulationDensityRecord(String code, String name, String source, int year, double population, double inlandAreaHectares, double totalAreaHectares, double populationPerHectare, double squareKilometres, double populationPerSquareKilometre)
	{
		this.code = code;
		this.name = name;
		this.source = source;
		this.year = year;
		this.population = population;
		this.inlandAreaHectares = inlandAreaHectares;
		this.totalAreaHectares = totalAreaHectares;
		this.populationPerHectare = populationPerHectare;
		this.squareKilometres = squareKilometres;
		this.populationPerSquareKilometre = populationPerSquareKilometre;
	}

	@DynamoDBAttribute(attributeName = "Code")
	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	@DynamoDBHashKey(attributeName = "Name")
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@DynamoDBAttribute(attributeName = "Source")
	public String getSource()
	{
		return source;
	}

	public void setSource(String source)
	{
		this.source = source;
	}

	@DynamoDBRangeKey(attributeName = "Year")
	public int getYear()
	{
		return year;
	}

	public void setYear(int year)
	{
		this.year = year;
	}

	@DynamoDBAttribute(attributeName = "Population")
	public double getPopulation()
	{
		return population;
	}

	public void setPopulation(double population)
	{
		this.population = population;
	}

	@DynamoDBAttribute(attributeName = "Inland_Area _Hectares")
	public double getInlandAreaHectares()
	{
		return inlandAreaHectares;
	}

	public void setInlandAreaHectares(double inlandAreaHectares)
	{
		this.inlandAreaHectares = inlandAreaHectares;
	}

	@DynamoDBAttribute(attributeName = "Total_Area_Hectares")
	public double getTotalAreaHectares()
	{
		return totalAreaHectares;
	}

	public void setTotalAreaHectares(double totalAreaHectares)
	{
		this.totalAreaHectares = totalAreaHectares;
	}

	@DynamoDBAttribute(attributeName = "Population_per_hectare")
	public double getPopulationPerHectare()
	{
		return populationPerHectare;
	}

	public void setPopulationPerHectare(double populationPerHectare)
	{
		this.populationPerHectare = populationPerHectare;
	}

	@DynamoDBAttribute(attributeName = "Square_Kilometres")
	public double getSquareKilometres()
	{
		return squareKilometres;
	}

	public void setSquareKilometres(double squareKilometres)
	{
		this.squareKilometres = squareKilometres;
	}

	@DynamoDBAttribute(attributeName = "Population_per_square_kilometre")
	public double getPopulationPerSquareKilometre()
	{
		return populationPerSquareKilometre;
	}

	public void setPopulationPerSquareKilometre(double populationPerSquareKilometre)
	{
		this.populationPerSquareKilometre = populationPerSquareKilometre;
	}
}
