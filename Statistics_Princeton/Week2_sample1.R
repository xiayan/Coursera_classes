# Statistics One, Lecture 6
# Read data, plot histograms

library(psych)

impact <- read.table("DAA.02.txt", header=T)
#class(impact)
#names(impact)

# Change default settings for graphics
# par()

#hist(impact$memory.visual, xlab="Visual memory", main="Histogram", col="red")
#describe(impact)
describe.by(impact, impact$cond)

# Y~X
#plot(impact$memory.verbal~impact$memory.visual, main="Scatterplot", ylab="Verbal memory", xlab="Visual memory")

# lm(Y~X)
#abline(lm(impact$memory.verbal~impact$memory.visual), col="blue")

# cor(X,Y)
#cor(impact$memory.verbal, impact$memory.visual)
#cor.test(impact$memory.verbal, impact$memory.visual)
aer <- impact[which (impact$cond == 'aer'),]
des <- impact[which (impact$cond == 'des'),]

new_aer <- aer[, 3:10]
new_des <- des[, 3:10]

cor(new_aer)
cor(new_des)

# Scatterplot matrix
#library(gclus)
#impact.r = abs(cor(impact))
#impact.col = dmat.color(impact.r)
#impact.o <- order.single(impact.r)
#cpairs(impact, impact.o, panel.colors=impact.col, gap=.5, main="Variable Ordered and Colored by Correlation")