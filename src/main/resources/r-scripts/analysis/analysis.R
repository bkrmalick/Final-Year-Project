#############-INPUT-###################
predict_date="30-08-2020"
predict_borough="Bexley"#"Westminster"
predict_borough_pd=4208.8
#predict_borough_cases=bdata

predict_year<-format(as.Date(predict_date,format="%d-%m-%Y"),"%Y")

#######################-PREPPING DATA-##################################
setwd("C:\\Users\\bkrma\\Google Drive\\Year 4\\Final Year Project")

data<-read.csv("phe_cases_london_boroughs.csv") 
data_pd<-read.csv("housing-density-borough.csv") 

data_pd<-data_pd[data_pd$Year==predict_year,]

##merge pop. density and cases data into one
data<-merge(x=data,y=data_pd,by.x="area_name",by.y="Name")

startdate <- as.Date("2020-02-10",format="%Y-%m-%d")
data<- data.frame(data,
                  day_index=as.numeric(as.character(round(
                    difftime(data$date,startdate ,units="days")
                  )))
)

bdata<-data[data$area_name==predict_borough,]

##data for one borough
summary(bdata)

###########################- basic graphs/curves -########################################

plot( bdata$date,bdata$total_cases, main = predict_borough, ylab="CASES",xlab="DATE") 
plot( data$date,data$total_cases, main = "All Boroughs", ylab="CASES",xlab="DATE") # APPENDIX

totalcases_by_date=tapply(data$total_cases,data$date,FUN=sum)
plot(
  data.frame(DATE=rownames(totalcases_by_date),CASES= totalcases_by_date),
  main="All Boroughs Combined"
)

##highers pop. density does not mean higher total cases as there are other variables at play
boxplot(data$total_cases~data$Population_per_square_kilometre, xlab="Pop. Density", ylab="Total Cases")

########################-Linear Model-##############################
lin_m<-lm(total_cases~day_index #+Population_per_square_kilometre
    ,data=data)

##ISSUE???????
plot(data$day_index,data$total_cases,ylab="CASES",xlab="DAY INDEX")
abline(lin_m, col="red", lwd=5)
legend("bottomright",legend=c("original data","linear model"),pch=c("o","-"),col=c("black","red"))

summary(lin_m)

#########################-SVM-################################

library(e1071)
set.seed(1)
bdata<-bdata[order(bdata$day_index),]
train<-1:(0.7*nrow(bdata))
bdata_train<-bdata[train,]
bdata_test<-bdata[-train,]

##SVM Regression
svm_cv<-tune( svm,
       total_cases~day_index, 
       #kernel="polynomial",
       data=bdata_train,
       ranges = list(epsilon = seq(0,1,0.1), cost = 2^(2:9)),
       )

summary(svm_cv$best.model)

plot(x=bdata_train$day_index, y=bdata_train$total_cases, col="blue", xlim=c(0,nrow(bdata)), ylim=c(0,max(bdata$total_cases)), ylab="TOTAL CASES", xlab="DAY INDEX")
points(x=bdata_test$day_index, y=bdata_test$total_cases, col="red")
legend("bottomright",legend=c("training data","test data", "predicted data"),pch=c("o","o", "x"),col=c("blue","red", "purple"))

isoreg_pred<-predict(svm_cv$best.model, bdata)
isoreg_pred<-isoreg(isoreg_pred)$yf
points(bdata$day_index,isoreg_pred,col = "purple", pch=4)

########################-Polynomial Regression -##############################
# find most optimal degree
error<-rep(0,10) #MSE

for(i in 1:10)
{
  lin_m<-lm(total_cases~poly(day_index,i), data=bdata)
  
  error[i]<-mean(
    (bdata$total_cases-predict(lin_m,bdata))^2
  )
}
plot(1:10,error, xlab="Degree", ylab="Mean Squared Error", type="b")

optimal_degree<-4 #which.min(error)

# train a model using it
optimal_m<-glm(total_cases~poly(day_index,optimal_degree),data=bdata_train)

plot(x=bdata_train$day_index, y=bdata_train$total_cases, col="blue", xlim=c(0,nrow(bdata)), ylim=c(0,max(bdata$total_cases)), ylab="TOTAL CASES", xlab="DAY INDEX")
points(x=bdata_test$day_index, y=bdata_test$total_cases, col="red")
legend("bottomright",legend=c("training data","test data", "predicted data"),pch=c("o","o", "x"),col=c("blue","red", "purple"))

isoreg_pred<-predict(optimal_m, bdata)
isoreg_pred<-isoreg(isoreg_pred)$yf
points(bdata$day_index,isoreg_pred,col = "purple", pch=4)

plot(bdata$day_index,bdata$total_cases)
points(
  bdata$day_index
  ,
  predict(optimal_m,bdata))


predict(lin_m,data.frame(day_index=200))

########################- Poisson Regression -##############################


# train a model using it
optimal_m<-glm(total_cases~day_index,data=bdata_train, family="poisson")

plot(x=bdata_train$day_index, y=bdata_train$total_cases, col="blue", xlim=c(0,nrow(bdata)), ylim=c(0,max(bdata$total_cases)), ylab="TOTAL CASES", xlab="DAY INDEX")
points(x=bdata_test$day_index, y=bdata_test$total_cases, col="red")
legend("bottomright",legend=c("training data","test data", "predicted data"),pch=c("o","o", "x"),col=c("blue","red", "purple"))
summary(optimal_m)
isoreg_pred<-predict(optimal_m, bdata)
#isoreg_pred<-isoreg(isoreg_pred)$yf
points(bdata$day_index,isoreg_pred,col = "purple", pch=4)

########################-lm BOROUGH using k-fold-##############################
library(boot)


error<-rep(0,10) #MSE

for(i in 1:10)
{
  set.seed(2)
  m<-glm(total_cases~poly(day_index,i), data=bdata)
  error[i]<-cv.glm(bdata,m,K=10)$delta[2]
}
plot(1:10,error, xlab="degree", ylab="mse error", type="b")

optimal_degree<-which.min(error)

optimal_m<-glm(total_cases~poly(day_index,3),data=bdata)

plot(bdata$day_index,bdata$total_cases, xlim=c(1,300),ylim=c(0,2000))
#predict original data
points(
  bdata$day_index,
  predict(optimal_m,bdata), col="red")

#predict future
points(1:300,
       predict(optimal_m,data.frame(day_index=1:300)), col="blue")


predict(optimal_m,data.frame(day_index=198))

#############################randomForest#######################

library(randomForest)
rf_model<-randomForest(
  total_cases~day_index+Population_per_square_kilometre, 
  data=data, 
  importance=TRUE)

predict(rf_model,
        newdata=data.frame(day_index=as.integer(300),
        Population_per_square_kilometre=predict_borough_pd)) #1164.681 

typeof(bdata$total_cases)
typeof(bdata$day_index)

varImpPlot(rf_model)


#############################randomForest borough#######################

rf_model<-randomForest(
  total_cases~day_index,
  data=bdata, 
  importance=TRUE)

plot(bdata$day_index,bdata$total_cases)

points(predict(rf_model,
        newdata=bdata) )

############################ISOTONE REGRESSION##########################
ir<-isoreg(bdata$day_index,bdata$total_cases)
plot(ir)


plot(isoreg(c(1,2,3,4,5),c(1,2,3,4,3)))