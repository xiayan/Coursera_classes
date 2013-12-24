# Statistics One, Lecture 12, example script
# Moderation analysis
# X is extraversion
# Y is happiness
# Z is SES

mod <- read.table("supplemental-STATS1.EX.06.txt", header = T)
no.mod.model = lm(mod$happy ~ mod$extra + mod$ses)
summary(no.mod.model)

mod.model = lm(mod$happy ~ mod$extra + mod$ses + mod$mod)
summary(mod.model)

anova(no.mod.model, mod.model)