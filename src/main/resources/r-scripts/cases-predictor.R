predictCasesUntilDate <-function(predict_date,bdata) 
{
  
#############-INPUT-###################
#predict_date="27-08-2020"
#predict_borough="Bexley"#"Westminster"

predict_year<-format(as.Date(predict_date,format="%d-%m-%Y"),"%Y")

#######################-PREPPING DATA-##################################

#oldest date for which we have data (10th of Feb 2020)
startdate<-bdata[which.min(bdata$date),"date"] 
startdate <- as.Date(startdate,format="%Y-%m-%d")-1 

predict_date <- as.Date(predict_date,format="%d-%m-%Y")
predict_day_index<-as.numeric(as.character(round(difftime(predict_date,startdate ,units="days"))))

bdata<-data.frame(bdata,
                  day_index=as.numeric(as.character(round(
                    difftime(bdata$date,startdate ,units="days")
                  )))
)

########################-lm BOROUGH using k-fold-##############################
#library(boot)

optimal_m<-glm(total_cases~poly(day_index,3),data=bdata)

#original data for borough
#plot(bdata$day_index,bdata$total_cases, xlim=c(1,250),ylim=c(0,2000))

#predict future for 300 days
#points(1:300,
#       predict(optimal_m,data.frame(day_index=1:300)), col="blue")

#existing data + predicted future
#plot(1:predict_day_index,
#      c(sort(bdata$total_cases, decreasing=F),round(predict(optimal_m,data.frame(day_index=predict_range)))))

pred<-predict(optimal_m,data.frame(day_index=1:predict_day_index))

# raw predicted
#plot(1:predict_day_index,pred, col="red")



# predicted - isoreg-ed (i.e made monotonic)
pred_corrected<-isoreg(predict(optimal_m,data.frame(day_index=1:predict_day_index)))$yf
#######################################UNCOMMENT lines 51,54,56 TO GET PRED vs ACTUAL PLOT#######################################
#plot(pred_corrected, col="blue", ylab="total_cases", xlab="day_index", ylim=c(-500,40000))

# true values
#points(bdata$day_index,bdata$total_cases, col="red")

#legend("bottomright",legend=c("original data","prediction model (isoreged)"),pch=c("o","o"),col=c("red","blue"))

################################-FINAL DF TO RETURN-################################################

##prediction data for all dates
#data.frame(date=as.Date(startdate+(1:predict_day_index)),
#           total_cases=round(pred_corrected)) 

##prediction data for two weeks only
#predict_range<-as.Date(startdate+((predict_day_index-14):predict_day_index) )
#final_df<-data.frame(date= predict_range,
                     #total_cases=round(tail(pred_corrected, 15))) 

##prediction data from start date up until predict date
predict_range<-as.Date(startdate+(1:predict_day_index) )

final_df<-data.frame(date= predict_range,
           total_cases=round(pred_corrected)) 

#final_df<-replaceRowsForWhichAlreadyData(final_df,bdata)

final_df<-data.frame(final_df,new_cases=calculateNewCasesColumn(final_df)) 

#tail(final_df,14) 
final_df
}

calculateNewCasesColumn<-function (df)
{
  new_cases<-rep(0,length(df$date))
  for(i in 1:length(df$date))
  {
    if(i!=1)
    {
      new_cases[i]<-df$total_cases[i]-df$total_cases[i-1]
    }
  }
  
  new_cases
}

replaceRowsForWhichAlreadyData<-function (df, bdata)
{
  dfToReturn<-df
  maxDateForWhichData<-as.Date(bdata[which.max(bdata$date),]$date)
  rowsToExclude<-c(which(as.Date(df$date)<=maxDateForWhichData))
  
  if(!isInteger0(rowsToExclude))
  {
    #remove prediction rows for which we already have data
    dfToReturn<-dfToReturn[-rowsToExclude,]
    
    #add back in the predictions to the existing data
    dfToReturn<-rbind(bdata[,c("date","total_cases")],dfToReturn)
  }
  
  row.names(dfToReturn)<-NULL
  dfToReturn
}

isInteger0 <- function(x)
{
  is.integer(x) && length(x) == 0L
}

DEBUG<-FALSE
if(DEBUG){
  # need to session > set working directory >  select current one
  source("dataToDebug.r") # load data frame from file because console cannot manage large dataframes
  predictions<-predictCasesUntilDate("21-09-2021",bdata)
  

  plot(bdata$total_cases, col="blue", xlim=c(0,nrow(bdata)+100), ylim=c(0,max(bdata$total_cases)+5000), ylab="TOTAL CASES", xlab="DAY INDEX")
  points(predictions$total_cases, col="red")
  legend("bottomright",legend=c("training data","model"),pch=c("o","o"),col=c("blue","red"))
}