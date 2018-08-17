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
invisible(library(ggcorrplot))
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
  
  if(action == "plot"){
    imgName <- as.character(length(list.files(outputDir)))
    png(file = paste(outputDir, "/", imgName, ".png", collapse = "", sep = ""), bg = "transparent")
    plot(mydata)
    invisible(dev.off())
    cat(paste("{\"outputFile\":\"", imgName, ".png", "\"}", collapse = "", sep = ""))
  } else if(action == "boxplot"){
    xName = args[4]
    yName = args[5]
    imgName <- as.character(length(list.files(outputDir)))
    g <- ggplot(mydata, aes_string(x=xName, y=yName)) + geom_boxplot() + theme(axis.text.x = element_text(angle = 45, hjust = 1))
    ggsave(filename=paste(outputDir, "/", imgName, ".png", collapse = "", sep = ""), plot=g)
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
    lmSummary <- summary(linearMod)
    stdError <- lmSummary$sigma
    plot(mydata[,xName], mydata[,yName], xlab=xName, ylab=yName)
    abline(linearMod)
    invisible(dev.off())
    cat(paste("{\"outputFile\":\"", imgName, ".png", "\", \"text\": \"Slope:", round(linearMod$coefficients[2], 3), " Intercept: ", round(linearMod$coefficients[1], 3), " Standard error: ", round(stdError, 3), "\"}", collapse = "", sep = ""))
  } else if(action == "correlation"){
    imgName <- as.character(length(list.files(outputDir)))
    corr <- round(cor(mydata), 1)
    g <- ggcorrplot(corr)
    ggsave(filename=paste(outputDir, "/", imgName, ".png", collapse = "", sep = ""), plot=g)
    cat(paste("{\"outputFile\":\"", imgName, ".png", "\"}", collapse = "", sep = ""))
  } else if(action == "trend-line"){
    xName = args[4]
    yName = args[5]
    imgName <- as.character(length(list.files(outputDir)))
    png(file = paste(outputDir, "/", imgName, ".png", collapse = "", sep = ""), bg = "transparent")
    scatter.smooth(x=mydata[,xName], y=mydata[,yName], main=as.formula(paste(yName, xName, sep= "~")), xlab=xName, ylab=yName)
    invisible(dev.off())
    cat(paste("{\"outputFile\":\"", imgName, ".png", "\"}", collapse = "", sep = ""))
  } else if(action == "columns"){
    cat(paste("{\"columns\":[\"", paste(colnames(mydata), collapse = "\",\""), "\"]}", collapse = "", sep = ""))
  }
}