#installOrIncludeLib <- function(x)
#{
#  if (!require(x,character.only = TRUE))
#  {
#    install.packages(x,dep=TRUE, repos='http://cran.us.r-project.org')
#    if(!require(x,character.only = TRUE)) stop("Package not found")
#  }
#}

#install.packages("caret")
invisible(library(caret))
invisible(library(ggplot2))
#installOrIncludeLib("caret")

args = commandArgs(trailingOnly=TRUE)

if (length(args)==0) {
  stop("Wrong number of arguments.")
} else {
  action <- args[1]
  inputFile <- args[2]
  outputDir <- args[3] #use only for plot and regression plot
  mydata <- read.csv(inputFile)
  attach(mydata)
  #factors <- c("factor1", "factor2")
  #as.formula(paste("y~", paste(factors, collapse="+")))
  
  if(action == "plot"){
    columnsParam <- args[4]
    columns <- c(unlist(strsplit(columnsParam,",")))
    filteredData <- mydata[columns]
    imgName <- as.character(length(list.files(outputDir)))
    png(file = paste(outputDir, "/", imgName, ".png", collapse = "", sep = ""), bg = "transparent")
    plot(filteredData)
    invisible(dev.off())
    cat(paste("{\"outputFile\":\"", imgName, ".png", "\"}", collapse = "", sep = ""))
  } else if(action == "summary"){
    summary(mydata)
  } else if(action == "regression"){
    regressionType = args[4]
    xName = args[5]
    yName = args[6]
    imgName <- as.character(length(list.files(outputDir)))
    png(file = paste(outputDir, "/", imgName, ".png", collapse = "", sep = ""), bg = "transparent")
    linearMod <- lm(as.formula(paste(yName, xName, sep= "~")), data=mydata)
    plot(mydata[,xName], mydata[,yName])
    abline(linearMod)
    invisible(dev.off())
    cat(paste("{\"outputFile\":\"", imgName, ".png", "\", \"intercept\":", linearMod$coefficients[1], ",\"slope\":", linearMod$coefficients[2], "}", collapse = "", sep = ""))
  } else if(action == "trend"){
    scatter.smooth(x=USGECropPercentCorn, y=ValuePerAcre, main="ValuePerAcre ~ USGECropPercentCorn")
  } else if(action == "columns"){
    cat(paste("{\"columns\":[\"", paste(colnames(mydata), collapse = "\",\""), "\"]}", collapse = "", sep = ""))
  }
}