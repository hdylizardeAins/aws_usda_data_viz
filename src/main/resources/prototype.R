#installOrIncludeLib <- function(x)
#{
#  if (!require(x,character.only = TRUE))
#  {
#    install.packages(x,dep=TRUE, repos='http://cran.us.r-project.org')
#    if(!require(x,character.only = TRUE)) stop("Package not found")
#  }
#}

#install.packages("caret")
library(caret)
library(ggplot2)
#installOrIncludeLib("caret")

args = commandArgs(trailingOnly=TRUE)

if (length(args)==0) {
  stop("Wrong number of arguments.")
} else {
  action <- args[1]
  inputFile <- args[2]
  outputDir <- args[3] #use only for plot
  columns <- args[4] #use only for plot
  regressionType = args[3] #use only for regressions
  mydata <- read.csv(inputFile)
  if(action == "plot"){
    columns <- c(unlist(strsplit(columns,",")))
    filteredData <- mydata[columns]
    imgName <- as.character(length(list.files(outputDir)))
    png(file = paste(outputDir, "/", imgName, ".png", collapse = "", sep = ""), bg = "transparent")
    plot(filteredData)
    invisible(dev.off())
    cat(paste("{\"outputFile\":\"", imgName, ".png", "\"}", collapse = "", sep = ""))
  } else if(action == "summary"){
    summary(mydata)
  } else if(action == "regression"){
    linearMod <- lm(ValuePerAcre ~ USGECropPercentCorn, data=mydata)
    #print(linearMod)
    cat(paste("{\"intercept\":", linearMod$coefficients[1], ",\"slope\":", linearMod$coefficients[2], "}", collapse = "", sep = ""))
  } else if(action == "columns"){
    cat(paste("{\"columns\":[\"", paste(colnames(mydata), collapse = "\",\""), "\"]}", collapse = "", sep = ""))
  }
}