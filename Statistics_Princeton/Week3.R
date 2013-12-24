# Statisics One, Lecture 9, example script
# Multiple regression analysis

library(psych)

endur <- read.table("supplemental-STATS1.EX.04.txt", header = T)

plot(endur$endurance ~ endur$age, main="Scatterplot", ylab = "Endurance", xlab = "Age")
abline(lm(endur$endurance ~ endur$age), col="blue")

plot(endur$endurance ~ endur$activeyears, ,main="Scatterplot", ylab = "Active Years")
abline(lm(endur$endurance ~ endur$activeyears), col='blue')

# Regression Analysis (unstandardized)
#model1 = lm(endur$endurance ~ endur$age)
#summary(model1)

#model2 = lm(endur$endurance ~ endur$activeyears)
#summary(model2)

#model3 = lm(endur$endurance ~endur$age + endur$activeyears)
#summary(model3)

# Regression analyses (standardized)
model1.z = lm(scale(endur$endurance) ~ scale(endur$age))
summary(model1.z)

model2.z = lm(scale(endur$endurance) ~ scale(endur$activeyears))
summary(model2.z)

model3.z = lm(scale(endur$endurance) ~ scale(endur$age) + scale(endur$activeyears))
summary(model3.z)

# Model comparisons
comp1 = anova(model1.z, model3.z)
comp1

comp2 = anova(model2.z, model3.z)
comp2

cor(endur)