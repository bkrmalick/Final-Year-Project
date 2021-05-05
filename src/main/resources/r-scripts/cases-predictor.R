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

optimal_m<-glm(total_cases~poly(day_index,4),data=bdata)

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
  #predictCasesUntilDate("10-09-2020",data.frame(date=as.Date(c(  "2020-02-11", "2020-02-12", "2020-02-13", "2020-02-14", "2020-02-15", "2020-02-16", "2020-02-17", "2020-02-18", "2020-02-19", "2020-02-20", "2020-02-21", "2020-02-22", "2020-02-23", "2020-02-24", "2020-02-25", "2020-02-26", "2020-02-27", "2020-02-28", "2020-02-29", "2020-03-01", "2020-03-02", "2020-03-03", "2020-03-04", "2020-03-05", "2020-03-06", "2020-03-07", "2020-03-08", "2020-03-09", "2020-03-10", "2020-03-11", "2020-03-12", "2020-03-13", "2020-03-14", "2020-03-15", "2020-03-16", "2020-03-17", "2020-03-18", "2020-03-19", "2020-03-20", "2020-03-21", "2020-03-22", "2020-03-23", "2020-03-24", "2020-03-25", "2020-03-26", "2020-03-27", "2020-03-28", "2020-03-29", "2020-03-30", "2020-03-31", "2020-04-01", "2020-04-02", "2020-04-03", "2020-04-04", "2020-04-05", "2020-04-06", "2020-04-07", "2020-04-08", "2020-04-09", "2020-04-10", "2020-04-11", "2020-04-12", "2020-04-13", "2020-04-14", "2020-04-15", "2020-04-16", "2020-04-17", "2020-04-18", "2020-04-19", "2020-04-20", "2020-04-21", "2020-04-22", "2020-04-23", "2020-04-24", "2020-04-25", "2020-04-26", "2020-04-27", "2020-04-28", "2020-04-29", "2020-04-30", "2020-05-01", "2020-05-02", "2020-05-03", "2020-05-04", "2020-05-05", "2020-05-06", "2020-05-07", "2020-05-08", "2020-05-09", "2020-05-10", "2020-05-11", "2020-05-12", "2020-05-13", "2020-05-14", "2020-05-15", "2020-05-16", "2020-05-17", "2020-05-18", "2020-05-19", "2020-05-20", "2020-05-21", "2020-05-22", "2020-05-23", "2020-05-24", "2020-05-25", "2020-05-26", "2020-05-27", "2020-05-28", "2020-05-29", "2020-05-30", "2020-05-31", "2020-06-01", "2020-06-02", "2020-06-03", "2020-06-04", "2020-06-05", "2020-06-06", "2020-06-07", "2020-06-08", "2020-06-09", "2020-06-10", "2020-06-11", "2020-06-12", "2020-06-13", "2020-06-14", "2020-06-15", "2020-06-16", "2020-06-17", "2020-06-18", "2020-06-19", "2020-06-20", "2020-06-21", "2020-06-22", "2020-06-23", "2020-06-24", "2020-06-25", "2020-06-26", "2020-06-27", "2020-06-28", "2020-06-29", "2020-06-30", "2020-07-01", "2020-07-02", "2020-07-03", "2020-07-04", "2020-07-05", "2020-07-06", "2020-07-07", "2020-07-08", "2020-07-09", "2020-07-10", "2020-07-11", "2020-07-12", "2020-07-13", "2020-07-14", "2020-07-15", "2020-07-16", "2020-07-17", "2020-07-18", "2020-07-19", "2020-07-20", "2020-07-21", "2020-07-22", "2020-07-23", "2020-07-24", "2020-07-25", "2020-07-26", "2020-07-27", "2020-07-28", "2020-07-29", "2020-07-30", "2020-07-31", "2020-08-01", "2020-08-02", "2020-08-03", "2020-08-04", "2020-08-05", "2020-08-06", "2020-08-07", "2020-08-08", "2020-08-09", "2020-08-10", "2020-08-11", "2020-08-12", "2020-08-13", "2020-08-14", "2020-08-15", "2020-08-16", "2020-08-17", "2020-08-18", "2020-08-19", "2020-08-20", "2020-08-21", "2020-08-22", "2020-08-23", "2020-08-24", "2020-08-25", "2020-08-26", "2020-08-27", "2020-08-28", "2020-08-29", "2020-08-30", "2020-08-31")), total_cases=c( 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,4,8,13,17,20,21,25,31,37,42,51,55,65,82,95,114,125,144,154,168,183,204,234,261,292,320,340,364,412,428,446,466,482,493,505,530,549,572,590,600,622,636,651,669,683,701,716,722,736,751,768,784,801,818,826,835,841,855,866,874,878,886,893,904,910,921,923,924,930,938,941,947,962,970,972,973,973,978,981,987,988,993,996,998,1000,1004,1004,1005,1005,1008,1010,1013,1016,1017,1019,1020,1020,1021,1022,1022,1023,1026,1030,1031,1032,1034,1034,1037,1038,1038,1039,1039,1043,1044,1046,1046,1046,1050,1051,1052,1058,1061,1062,1063,1066,1068,1070,1070,1073,1076,1076,1080,1084,1084,1088,1092,1095,1098,1098,1102,1102,1104,1105,1106,1106,1106,1106,1106,1109,1111,1113,1114,1116,1118,1120,1123,1126,1128,1128,1128,1129,1133,1138,1145,1146,1148,1150,1152,1154,1157,1161,1167,1174,1176,1176)))
  
  # need to session > set working directory >  select current one
  source("dataToDebug.r") # load data frame from file because console cannot manage large dataframes
  r<-predictCasesUntilDate("21-06-2021",x)
  
  plot(x)
  plot(r[,c("date","total_cases")])
}