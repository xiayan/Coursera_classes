# Statistics One, Lecture 12, example script
# Mediation analysis
# X is extraversion
# Y is happiness
# M is diversity of life experience

library(psych)
library(multilevel)

med <- read.table("supplemental-STATS1.EX.05.txt", header = T)
par(ask=T)
describe(med)
layout(matrix(c(1,2,3,4), 2, 2, byrow = TRUE))
hist(med$happy, breaks = 6)
hist(med$extra, breaks = 6)
hist(med$diverse, breaks = 6)

# test linear and homescedasticity
layout(matrix(c(1,2,3,4), 2, 2, byrow = TRUE))
plot(med$happy ~ med$extra)
abline(lm(med$happy ~ med$extra))
plot(med$diverse ~ med$extra)
abline(lm(med$diverse ~ med$extra))
plot(med$happy ~ med$diverse)
abline(lm(med$happy ~ med$diverse))

# Conduct three regression analyses
model1 = lm(med$happy ~ med$extra)
summary(model1)
model2 = lm(med$diverse ~ med$extra)
summary(model2)
model3 = lm(med$happy ~ med$extra + med$diverse)
summary(model3)

# Sobel test (is the indirect path statistically significant?)
indirect = sobel(med$extra, med$diverse, med$happy)
indirect