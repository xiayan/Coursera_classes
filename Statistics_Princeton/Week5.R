library(psych)
library(car)

daa = read.table("DAA.04.txt", header = T)
read.table()
read.

#describe.by(wm, wm$cond)

daa.sp = subset(daa, daa$training == "SP")
daa.wm = subset(daa, daa$training == "WM")

daa.sp.out = describe(daa.sp)
daa.sp.out

daa.wm.out = describe(daa.wm)
daa.wm.out

# depedent t-tests for SP group
t.test(daa.sp$pre.uat, daa.sp$post.uat, paired = T)
d.sp = (daa.sp.out[4,3]) / (daa.sp.out[4,4])
d.sp

# depedent t-tests for WM group
t.test(daa.wm$pre.uat, daa.wm$post.uat, paired = T)
d.wm = (daa.wm.out[4,3]) / (daa.wm.out[4,4])
d.wm

# Independent t-tests
t.test(daa$gain ~ daa$training, var.equal = T)
pooled.sd = (daa.sp.out[4,4] + daa.wm.out[4,4]) / 2
d.ct = (daa.sp.out[4,3] - daa.wm.out[4,3]) / pooled.sd
d.ct

# ANOVA
aov.model = aov(wm.t$gain ~ wm.t$cond)
summary(aov.model)
aov.table = summary(aov.model)

# Effect size for ANOVA
ss = aov.table[[1]]$"Sum Sq"
eta.sq = ss[1] / (ss[1] + ss[2])
eta.sq

# Post-hoc tests
TukeyHSD(aov.model)

# Levene's test
library(car)
leveneTest(wm.t$gain, wm.t$cond, center = "mean")
