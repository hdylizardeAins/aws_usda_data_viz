#install.packages("caret")
library(caret)
mydata <- read.csv("C:/Users/ewimberley/Desktop/USDA Prototype/geneticEngineeringAdoption.csv")
#featurePlot(x=iris[,1:5], y=iris[,5], plot="pairs", auto.key=list(columns=3))
plot(mydata)
summary(mydata)

linearMod <- lm(ValuePerAcre ~ USGECropPercentCorn, data=mydata)
print(linearMod)