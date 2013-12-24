# Coursera Statistics One, Lecture 3, example script
# Read data into a dataframe, and generate histogram and descriptives

# Load Psych library
library(psych)

# Read the data into a dataframe called ratings
data <- read.table("DAA.01.txt", header = TRUE)

des<-data[which(data$cond=="des"),]
aer<-data[which(data$cond=="aer"),]

# Print 4 histograms on one page
# Plot four histograms on one page
# c stands for "concantenate"
layout(matrix(c(1:8), 4, 2, byrow = TRUE))
# $ picks out one column
hist(des$pre.wm.s)
hist(des$pre.wm.v)
hist(des$post.wm.s)
hist(des$post.wm.v)
hist(aer$pre.wm.s)
hist(aer$pre.wm.v)
hist(aer$post.wm.s)
hist(aer$post.wm.v)

#hist(ratings$WoopWoop, xlab = "Rating")
#hist(ratings$RedTruck, xlab = "Rating")
#hist(ratings$HobNob, xlab = "Rating")
#hist(ratings$FourPlay, xlab = "Rating")